package http;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private BufferedReader bufferedReader;
    private final String[] firstLines;
    private final Map<String, String> headers;
    private final int contentLength;
    private final Map<String, List<String>> parameters;

    public HttpRequest(InputStream inputStream) throws IOException {
        this.bufferedReader = toBufferReader(inputStream);
        this.firstLines = firstLines(getFirstLine());
        this.headers = initHeader();
        this.contentLength = getContentLength();
        this.parameters = initParameter();
    }

    public String getPath() {
        return splitFromSeparator(this.firstLines[1], "\\?")[0];
    }


    public Method getMethod() {
        return Method.valueOf(this.firstLines[0]);
    }

    public String getHeader(String key) {
        if (!this.headers.containsKey(key)) {
            return "";
        }
        return this.headers.get(key);
    }

    private int getContentLength() {
        if (!this.headers.containsKey("Content-Length")) {
            return 0;
        }
        return Integer.parseInt(this.headers.get("Content-Length"));
    }

    public List<String> getParameter(String key) throws IOException {
        return this.parameters.get(key);
    }

    private Map<String, List<String>> parseParam(String line) {
        String[] kvs = line.split("&");
        Map<String, List<String>> map = new HashMap<>();
        for (String kv : kvs) {
            String[] splitFromSeparator = splitFromSeparator(kv, "=");
            String key = splitFromSeparator[0];
            String value = splitFromSeparator[1];
            if (map.containsKey(key)) {
                map.get(key).add(value);
            }else{
                map.put(key, List.of(value));
            }
        }
        return map;
    }

    private Map<String, List<String>> initParameter() throws IOException {
        // url parsing, content-length X
        // body parsing, content-length O

        if (this.getMethod().equals(Method.GET)) {
            String urlKeyAndValues = splitFromSeparator(this.firstLines[1], "\\?")[1];
            return parseParam(urlKeyAndValues);
        }else{
            if (contentLength != 0) {
                char[] body = new char[contentLength];
                this.bufferedReader.read(body, 0, contentLength);
                String line = String.copyValueOf(body);
                return parseParam(line);
            }
        }
        return null;
    }

    public Map<String, String> initHeader() throws IOException {
        // init header
        log.info("start processing parse request from inputStream ");
        String line;
        Map<String, String> headers = new HashMap<>();
        while ((line = this.bufferedReader.readLine()).length() != 0) {
            String[] kv = splitFromSeparator(line, ": ");
            headers.put(kv[0], kv[1]);
        }
        log.info("end processing parse request from inputStream ");
        headers.keySet().forEach(key -> System.out.println(
                "header key : " + key + ", value : " + headers.get(key)
        ));
        return headers;
    }

    private String[] firstLines(String firstLine) {
        return this.splitFromSeparator(firstLine, " ");
    }

    private String[] splitFromSeparator(String line, String separator) {
        return line.split(separator);
    }

    private String getFirstLine() throws IOException {
        return this.bufferedReader.readLine();
    }


    private static BufferedReader toBufferReader(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }
}
