package util;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequestUtilsTest{
    private static final Logger log = LoggerFactory.getLogger(CustomHttpRequestUtilsTest.class);

    private static String url;


    @Before
    public void setUp() throws Exception {
        url = "/user/create?userId=javagiki&password=javagiki11&name=minshik";
    }

    @Test
    public void splitPathAndParam() {
        String path = UrlParser.parsePath(url);


        // then
        Assert.assertEquals("/user/create", path);
    }

    @Test
    public void parseParam() {
        String param = UrlParser.getParam(url);
        Assert.assertEquals("userId=javagiki&password=javagiki11&name=minshik",param);
    }

    @Test
    public void getKeyAndValueFromURlParam() {

        // when
        Map<String, String> map = UrlParser.parseParam(url);

        // then
        Assert.assertEquals(map.get("userId"), "javagiki");
        Assert.assertEquals(map.get("password"), "javagiki11");
        Assert.assertEquals(map.get("name"), "minshik");
    }
}

class UrlParser{

    public static String parsePath(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

    public static String getParam(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return null;
        }
        return url.substring(index + 1);
    }

    // parser 기능과 map 넣는 기능 두가지를 담당?
    public static Map<String, String> parseParam(String url) {
        Map<String, String> paramMap = new HashMap<>();

        String params = UrlParser.getParam(url);
        String[] spliterator = params.split("&");
        for (String keyValue : spliterator) {
            // put map key and value
            String[] split = keyValue.split("=");
            String key = split[0];
            String value = split[1];
            paramMap.put(key, value);
        }

        return paramMap;
    }

    private static Map<String, String> parseKeyValue(String splitedParam) {
        Map<String, String > kv = new HashMap<>();
        String[] split = splitedParam.split("=");
        kv.put(split[0], split[1]);
        return kv;
    }
}
