package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // save user
    public void save(CreateUserRequest userRequest) {
        log.info("userRequest : {}", userRequest);
        User user = User.create(userRequest);
        DataBase.addUser(user);
    }
}
