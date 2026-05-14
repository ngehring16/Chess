package service;

import chessrecords.RegisterResult;
import chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GettingStartedTest {

    @Test
    void register() throws AlreadyTakenException {
        GettingStarted starter = new GettingStarted();
        UserData user = new UserData("ngehring", "password", "hello@gmail.com");
        RegisterResult result = starter.register(user);
        Assertions.assertEquals(result.username(), user.username());
    }

    @Test
    void login() {
    }
}