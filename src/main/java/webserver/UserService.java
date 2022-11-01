package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // save user
    public void save(CreateUserRequest userRequest) {
        log.info("userRequest : {}", userRequest);
        User user = User.create(userRequest);
        DataBase.addUser(user);

        User findUser = DataBase.findUserById(user.getUserId());
        log.info("save user : {}", findUser);
    }

    // login
    public boolean login(String userId, String password) {
        User user = DataBase.findUserById(userId);
        return user.matchPassword(password);
    }
}
