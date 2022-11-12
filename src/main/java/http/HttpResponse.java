package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dos;
    public HttpResponse(OutputStream outPutStream) {
        dos = new DataOutputStream(outPutStream);
    }

    public void forward(String viewPath) throws IOException {
        // view path 위치에 있는 파일을 내보낸다.
        // 1. 읽기
        byte[] body = Files.readAllBytes(new File("./webapp/" + viewPath).toPath());

        // 2. header 와 body 만들기
        responseBody(body);
    }


    public void sendRedirect(String s) {
    }

    public void addHeader(String s, String s1) {

    }

    private void responseBody(byte[] body) {
        try {
            this.dos.write(body, 0, body.length);
            this.dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
