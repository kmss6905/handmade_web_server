package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomHttpRequestUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(CustomHttpRequestUtilsTest.class);

    @Test
    public void parseQueryParamInStringURL() {

        String url = "/user/create?";
        int index = url.indexOf("?");
        if (index == -1) {
            log.info("requestPath : {}", url);
        }else{
            String requestPath = url.substring(0, index);
            log.info("requestPath: {}", requestPath);
        }
    }
}
