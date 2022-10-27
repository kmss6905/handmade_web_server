package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Stream;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private UserService userService;

    public RequestHandler(Socket connectionSocket) {
        this.userService = new UserService();
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String r;
            byte[] bytes;
            if ((r = bufferedReader.readLine()) != null) {
                log.info(r);
                String[] split = r.split(" ");
                String url = split[1];

                // error 발생 가능성이 있다.
                String requestPath = HttpRequestUtils.getPath(url);
                String params = HttpRequestUtils.getParam(url);
                if(requestPath.equals("/index.html")){
                    bytes = Files.readAllBytes(new File(("./webapp" + split[1])).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, bytes.length);
                    responseBody(dos, bytes);
                } else if (requestPath.equals("/user/form.html")) {
                    log.info("로그인 페이지 요청입니다.");
                    // 파일읽기

                    bytes = Files.readAllBytes(new File(("./webapp" + split[1])).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, bytes.length);
                    responseBody(dos, bytes);
                } else if(requestPath.equals("/user/create")){
                    log.info("회원가입 생성 요청");

                    // request parsing
                    Map<String, String> map = HttpRequestUtils.parseQueryString(params);
                    CreateUserRequest userRequest = CreateUserRequest.of(map);

                    // save user
                    userService.save(userRequest);



                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = "HelloWorld".getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);

                } else {
                    // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = "HelloWorld".getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            }

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
}
