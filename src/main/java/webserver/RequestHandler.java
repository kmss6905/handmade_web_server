package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import com.google.common.base.Strings;
import http.Method;
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
            String r;
            byte[] bytes;


            String firstLine = br.readLine();
            Method method = getMethod(firstLine);
            String path = getPath(firstLine);
            String params = getQueryParam(firstLine);



            log.info("method : {}, path : {}, params : {}", method, path, params);
            int contentLength = 0;
            // header
            while ((r = br.readLine()).length() != 0) {
                log.info("header info : {}", r);
                if (r.contains("Content-Length")) {
                    contentLength = getContentLength(r);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            switch (method) {

                case GET:
                default:
                    if (path.equals("/user/form.html")) {
                        // body
                        bytes = Files.readAllBytes(new File(("./webapp" + path)).toPath());
                    }else{
                        // default
                        bytes = "HelloWorld".getBytes();
                    }
                    response200Header(dos, bytes.length);
                    responseBody(dos, bytes);
                case POST:
                    StringBuilder content = new StringBuilder();
                    if (path.equals("/user/create")) {
                        // request content 의 길이를 구해야한다.
                        int i = 0;
                        // 작동을 하지 않는다?
                        while ((i = br.read()) != -1) {
                            char c = (char) i;
                            content.append(c);
                            log.info("content : {}", content);
                        }
                        log.info("while 문 빠져나온다. content={}", content);

//                        log.info("contentLength : {}", contentLength);
//                        String s = IOUtils.readData(br, contentLength);
//                        log.info("s : {}", s);

                        bytes = "HelloWorld".getBytes();
                        response200Header(dos, bytes.length);
                        responseBody(dos, bytes);
                    }
                case PATCH:
                case DELETE:
            }

            log.info("first line : {}", firstLine);

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

    private void parseBody(String line) {

    }
}
