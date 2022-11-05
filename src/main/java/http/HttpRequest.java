package http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private BufferedReader bufferedReader;
    private final String firstLine;

    public HttpRequest(InputStream inputStream) throws IOException {
        this.bufferedReader = toBufferReader(inputStream);
        this.firstLine = getFirstLine();
    }

    private String getFirstLine() throws IOException {
        return this.bufferedReader.readLine();
    }

    public Method getMethod() {
        assert this.bufferedReader != null;
        String[] s = this.firstLine.split(" ");
        String method = s[0];
        return Method.valueOf(method);
    }

    public String getHeader(String key) {
        return "";
    }

    public String getURL() {
        return "";
    }

    private static BufferedReader toBufferReader(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }
}
