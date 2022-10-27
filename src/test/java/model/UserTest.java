package model;

import db.DataBase;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class UserTest{


    @Test
    public void saveUser() {
        // user 를 저장합니다.
        User user = User.of("a", "b", "c", "d");
        DataBase.addUser(user);

        User findUser = DataBase.findUserById("a");
        Assert.assertEquals(findUser, user);
    }

    @Test
    public void testGetUserId() {

    }

    @Test
    public void testGetPassword() {
    }

    @Test
    public void testTestGetName() {
    }

    @Test
    public void testGetEmail() {
    }
}