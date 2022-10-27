package webserver;


import com.google.common.base.Strings;

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
        createUserRequest.name = createUserParam.get("name");
        createUserRequest.password = createUserParam.get("password");
        return createUserRequest;
    }

    private static String getUserID(Map<String, String> createUserParam) {
        // null check
        if (createUserParam.get("userId") == null) {

        }
        return createUserParam.get("userId");
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
