package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import com.google.common.base.Strings;
import db.DataBase;
import http.Method;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private UserService userService;

    // 스레드 생성
    public RequestHandler(Socket connectionSocket) {
        this.userService = new UserService();
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            byte[] bytes;


            String firstLine = br.readLine();
            Method method = getMethod(firstLine);
            String path = getPath(firstLine);
            String params = getQueryParam(firstLine);
            log.info("method : {}, path : {}, params : {}", method, path, params);
            int contentLength = 0;
            boolean logined = false;
            Map<String, String> cookies = new HashMap<>();
            // header
            while ((line = br.readLine()).length() != 0) {
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }

                if (line.contains("Cookie")) {
                    logined = isLogin(line);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            switch (method) {

                case GET:
                default:
                    if (path.endsWith(".css")) {
                        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
                        response200CssHeader(dos, body.length);
                        responseBody(dos, body);
                    }

                    if (path.equals("/user/list")) {
                        try {
                            if (logined) {
                                log.info("로그인 한 유저임!!");
                                // 전체유저
                                Collection<User> users = DataBase.findAll();
                                StringBuilder sb = new StringBuilder();
                                sb.append("<table border='1'>");
                                for (User user : users) {
                                    sb.append("<tr>");
                                    sb.append("<td>" + user.getUserId() + "</td>");
                                    sb.append("<td>" + user.getName() + "</td>");
                                    sb.append("<td>" + user.getEmail() + "</td>");
                                    sb.append("</tr>");
                                }
                                sb.append("</table>");
                                bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
                            }else{
                                bytes = Files.readAllBytes(new File(("./webapp" + "/user/login.html")).toPath());
                            }
                        } catch (NullPointerException e) {
                            bytes = Files.readAllBytes(new File(("./webapp" + "/user/login.html")).toPath());
                        }
                    }else{
                        bytes = Files.readAllBytes(new File(("./webapp" + path)).toPath());
                    }
                    response200Header(dos, bytes.length);
                    responseBody(dos, bytes);
                case POST:
                    if (path.equals("/user/login")) {
                        String body = IOUtils.readData(br, contentLength);
                        Map<String, String> map = HttpRequestUtils.parseQueryString(body);
                        String userId = map.get("userId");
                        String password = map.get("password");
                        boolean isLogin = userService.login(userId, password);
                        if (isLogin) {
                            response302HeaderLoginResult(dos, isLogin);
                        }else{
                            bytes = Files.readAllBytes(new File(("./webapp" + "/user/login_failed.html")).toPath());
                            response200Header(dos, bytes.length);
                            responseBody(dos, bytes);
                        }
                    }
                    if (path.equals("/user/create")) {
                        String body = IOUtils.readData(br, contentLength);
                        Map<String, String> map = HttpRequestUtils.parseQueryString(body);

                        // 비지니스 로직
                        userService.save(CreateUserRequest.of(map));

                        response302Header(dos);
                    }
                case PATCH:
                case DELETE:
            }

            log.info("first line : {}", firstLine);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loginReponseHeader(DataOutputStream dos, int lengthOfBodyContent, boolean isLogin) {
        if (isLogin) {
            response302Header(dos);
        }
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css; \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderLoginResult(DataOutputStream dos, boolean login) {
        if (login) {
            try {
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Set-Cookie: logined=true \r\n");
                dos.writeBytes("Location: /index.html \r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }else{

        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Method getMethod(String firstLine) {
        if (Strings.isNullOrEmpty(firstLine)) {
            throw new IllegalArgumentException("can not be null first line");
        }
        String method = firstLine.split(" ")[0];
        return Method.getMethod(method);
    }

    private String getPath(String firstLine) {
        if (Strings.isNullOrEmpty(firstLine)) {
            throw new IllegalArgumentException("can not be null first line");
        }
        String url = firstLine.split(" ")[1];
        return HttpRequestUtils.getPath(url);
    }

    private String getQueryParam(String firstLine) {
        if (Strings.isNullOrEmpty(firstLine)) {
            throw new IllegalArgumentException("can not be null first line");
        }
        String url = firstLine.split(" ")[1];
        return HttpRequestUtils.getParam(url);
    }

    private void parseHeader(String line) throws IOException {

    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    private boolean isLogin(String line) {
        String[] headerTokens = line.split(":");
        String[] cookies = headerTokens[1].split("; ");
        List<HttpRequestUtils.Pair> pairs = new ArrayList<>();
        for (String cookie : cookies) {
            System.out.println("cookie : " + cookie);
            pairs.add(splitKeyAndValue(cookie, "="));
        }
        return pairs.stream().anyMatch(it -> it.getKey().equals("logined") && it.getValue().equals("true"));
    }

    private HttpRequestUtils.Pair splitKeyAndValue(String kv, String splitor) {
        String[] split = kv.split(splitor);
        String key = split[0].trim();
        String value = split[1].trim();
        return new HttpRequestUtils.Pair(key, value);
    }
}
