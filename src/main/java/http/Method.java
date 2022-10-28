package http;

import java.util.Arrays;

public enum Method {
    GET, POST, DELETE, PATCH;

    public static Method getMethod(String method) {
        return Arrays.stream(Method.values())
                .filter(it -> it.name().equals(method))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
