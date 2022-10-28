package webserver;


import com.google.common.base.Strings;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// 회원가입 요청
public class CreateUserRequest {
    private String userId;
    private String password;
    private String email;
    private String name;

    private CreateUserRequest() {}

    public static CreateUserRequest of(Map<String, String> createUserParam) {
        nullCheck(createUserParam);
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.email = createUserParam.get("email");
        createUserRequest.userId = createUserParam.get("userId");
        createUserRequest.name = URLDecoder.decode(createUserParam.get("name"), StandardCharsets.UTF_8);
        createUserRequest.password = createUserParam.get("password");
        return createUserRequest;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    private static void nullCheck(Map<String, String> createUserParam) {
        if (Strings.isNullOrEmpty(createUserParam.get("userId"))) {
            throw new IllegalArgumentException();
        }

        if (Strings.isNullOrEmpty(createUserParam.get("password"))) {
            throw new IllegalArgumentException();
        }


        if (Strings.isNullOrEmpty(createUserParam.get("email"))) {
            throw new IllegalArgumentException();
        }

        if (Strings.isNullOrEmpty(createUserParam.get("name"))) {
            throw new IllegalArgumentException();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
