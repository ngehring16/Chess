package service;

import chessrecords.RegisterResult;
import chessrecords.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GettingStartedTest {
    private final GettingStarted starter = new GettingStarted();
    @Test
    void registerPositive() throws AlreadyTakenException, DataAccessException {
        UserData user = new UserData("ngehring", "password", "hello@gmail.com");
        RegisterResult result = starter.register(user);
        Assertions.assertEquals(result.username(), user.username());
   }
   @Test
   void registerNegative() {
        UserData user = new UserData(null, "null", "null");
        Assertions.assertThrows(DataAccessException.class, ()->{starter.register(user);});
   }

    @Test
    void login() {
    }
}