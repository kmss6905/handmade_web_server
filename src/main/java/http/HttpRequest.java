package http;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private BufferedReader bufferedReader;
    private final String[] firstLines;
    private final Map<String, String> headers;

    public HttpRequest(InputStream inputStream) throws IOException {
        this.bufferedReader = toBufferReader(inputStream);
        this.firstLines = firstLines(getFirstLine());
        this.headers = initHeader();
    }

    public String getPath() {
        return this.firstLines[1];
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

    public Map<String, String> initHeader() throws IOException {
        // init header
        log.info("start processing parse request from inputStream ");
        String l;
        Map<String, String> headers = new HashMap<>();
        while (!(l = this.bufferedReader.readLine()).equals("")) {
            String[] kv = splitFromSeparator(l, ": ");
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
