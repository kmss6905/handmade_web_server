package http;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;

public class HttpResponseTest{
    private static final Logger log = LoggerFactory.getLogger(HttpResponseTest.class);
    private static final String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws IOException {
        HttpResponse response = new HttpResponse(createOutPutStream("Http_Forward.html"));

        // when
        response.forward("index.html");


        String path = testDirectory + "Http_Forward.html";
        byte[] result = Files.readAllBytes(new File(path).toPath());
        byte[] expected = Files.readAllBytes(new File("./webapp/index.html").toPath());

        // then
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void responseRedirect() throws FileNotFoundException {
        HttpResponse response = new HttpResponse(createOutPutStream("Http_Redirect.txt"));
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws FileNotFoundException {
        HttpResponse response = new HttpResponse(createOutPutStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutPutStream(String fileName) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + fileName));
    }
}