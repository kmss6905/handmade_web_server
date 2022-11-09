package http;

import java.util.HashMap;
import java.util.Map;

public class HeaderUtil {


    public static Map<String, String> cssHeader() {
        return null;
    }


    public static Map<String, String> header200(int lengthOfBodyContent) {
//        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", String.valueOf(lengthOfBodyContent));
        return null;
    }
}
