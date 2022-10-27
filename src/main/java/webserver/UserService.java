package webserver;

import db.DataBase;
import model.User;

public class UserService {

    // save user
    public void save(CreateUserRequest userRequest) {
        User user = User.create(userRequest);
        DataBase.addUser(user);
    }
}
