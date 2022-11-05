package http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import java.io.*;

public class HttpRequestTest {



    @Test
    public void get() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/java/example/Http_Get.txt"));
        String r;
        while ((r = bufferedReader.readLine()) != null) {
            System.out.println(r);
        }

        // request.getMethod()
        // request.getParam
        // request.getURL
    }


    @Test
    public void HTTPRequest객체로부터메서드를반환한다() throws FileNotFoundException {
        InputStream is = new FileInputStream("/Users/minshik/Desktop/study/java-nextstep/handmade_web_server/src/test/java/example/Http_Get.txt");
        InputStreamReader reader = new InputStreamReader(is);
    }


}