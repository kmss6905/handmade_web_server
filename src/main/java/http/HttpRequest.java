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

    public String getPath() {
        String[] s = firstLines(this.firstLine);
        return s[1];
    }
    public Method getMethod() {
        assert this.bufferedReader != null;
        String[] s = firstLines(this.firstLine);
        return Method.valueOf(s[0]);
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
