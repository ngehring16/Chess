package service;

import chessrecords.AlreadyTakenException;
import chessrecords.RegisterResult;
import chessrecords.UserData;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GettingStartedTest {
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final UserDataAccess userAccess = new UserDataAccess();
    private final GettingStarted starter = new GettingStarted(authAccess, userAccess);
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