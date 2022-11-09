package http;

import junit.framework.TestCase;
import org.junit.Test;

public class HttpResponseTest{

    @Test
    public void responseForward() {
        HttpResponse response = new HttpResponse();
        response.forward("index.html");
    }

    @Test
    public void responseRedirect(){
        HttpResponse response = new HttpResponse();
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() {
        HttpResponse response = new HttpResponse();
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }


}