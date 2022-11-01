package model;

import webserver.CreateUserRequest;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    protected User() {}

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public static User create(CreateUserRequest request) {
        User user = new User();
        user.name = request.getName();
        user.password = request.getPassword();
        user.email = request.getEmail();
        user.userId = request.getUserId();
        return user;
    }

    public static User of(String userId, String password, String email, String name) {
        User user = new User();
        user.name = name;
        user.password = password;
        user.email = email;
        user.userId = userId;
        return user;
    }
}
