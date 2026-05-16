package service;

import chessrecords.*;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GettingStartedTest {
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final UserDataAccess userAccess = new UserDataAccess();
    private final GettingStarted starter = new GettingStarted(authAccess, userAccess);
    private final UserData user = new UserData("ngehring", "password", "hello@gmail.com");
    @Test
    void registerWithValidRequestPositive() throws AlreadyTakenException, DataAccessException {
        RegisterResult result = starter.register(user);
        Assertions.assertEquals(result.username(), user.username());
   }
   @Test
   void registerWithInvalidRequestNegative() {
        UserData user = new UserData(null, "null", "null");
        Assertions.assertThrows(DataAccessException.class, ()->{starter.register(user);});
   }

    @Test
    void loginWithAccurateAuthTokenPositive() throws DoesNotExistException, DoesNotMatchException, DataAccessException, AlreadyTakenException {
        starter.register(user);
        LoginRequest request = new LoginRequest("ngehring", "password");
        RegisterResult result = starter.login(request);
        Assertions.assertEquals(result.authToken(), authAccess.getAuth(result.authToken()).authToken());
    }

    @Test
    void loginWithTheWrongPasswordNegative() throws DataAccessException, AlreadyTakenException {
        starter.register(user);
        LoginRequest request = new LoginRequest("ngehring", "incorrectPassword");
        Assertions.assertThrows(DoesNotMatchException.class, ()->{starter.login(request);});
    }
}