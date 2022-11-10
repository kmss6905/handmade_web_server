package http;

import java.io.OutputStream;
import java.util.Map;

public class HttpResponse {
    private Map<String, String> header;

    public HttpResponse(OutputStream outPutStream) {

    }

    public void forward(String viewPath) {
//        dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
//        dos.writeBytes("Set-Cookie: logined=true \r\n");
//        dos.writeBytes("Location: /index.html \r\n");
//        dos.writeBytes("\r\n");
    }


    public void sendRedirect(String s) {
    }

    public void addHeader(String s, String s1) {

    }
}
