package http;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static http.Method.GET;
import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestTest.class);


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

    @Test
    public void HttpRequest로부터_parameter를_반환한다_GET() throws IOException {
        // given
        InputStream inputStream = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/resources/Http_Get.txt");

        // when
        HttpRequest request = new HttpRequest(inputStream);

        // then
        Assert.assertEquals(List.of("minshikkim"), request.getParameter("userName"));
        Assert.assertEquals(List.of("alstlrdl1!"), request.getParameter("password"));
    }

    @Test
    public void HttpRequest로부터_parameter를_반환한다_POST() throws IOException {
        // given
        InputStream inputStream = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/resources/Http_Post.txt");

        // when
        HttpRequest request = new HttpRequest(inputStream);

        // then
        Assert.assertEquals(List.of("minshikkim"), request.getParameter("userName"));
        Assert.assertEquals(List.of("alstlrdl1!"), request.getParameter("password"));
    }


    @Test
    public void parseParameter() {
        String parameters = "userName=minshik&password=alstlrdl1!";
        String[] split = parameters.split("&");
        Map<String, String> map = new HashMap<>();
        Arrays.stream(split)
                .forEach(it -> {
                    String[] split1 = it.split("=");
                    String key = split1[0];
                    String value = split1[1];
                    map.put(key, value);
                });
        map.keySet().forEach(it -> {
                    log.info(" key : {} , value : {}", it, map.get(it));
                });
    }

}