package http;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static http.Method.GET;
import static org.junit.Assert.assertEquals;

public class HttpRequestTest {



    @Test
    public void get() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/java/example/Http_Get.txt"));
        String r;
        while ((r = bufferedReader.readLine()) != null) {
            System.out.println(r);
        }
    }


    @Test
    public void HTTPRequest로부터_HTTP메서드를_반환한다() throws IOException {
        // given
        InputStream inputStream = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/resources/Http_Get.txt");
        HttpRequest httpRequest = new HttpRequest(inputStream);

        // then
        assertEquals(GET, httpRequest.getMethod());
    }

    @Test
    public void HttpRequest로부터_PATH를_반환한다() throws IOException{
        // given
        InputStream inputStream = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/resources/Http_Get.txt");
        HttpRequest httpRequest = new HttpRequest(inputStream);

        // then
        assertEquals("/hello", httpRequest.getPath());
    }

    @Test
    public void HttpRequest로부터_header를_반환한다() throws IOException{
        // given
        InputStream inputStream = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/resources/Http_Get.txt");

        // when
        HttpRequest request = new HttpRequest(inputStream);


        // then
        assertEquals("gzip",request.getHeader("Content-Encoding"));
        assertEquals("LiteSpeed",request.getHeader("Server"));
    }

}