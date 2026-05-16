package service;

import chessrecords.*;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClearAllTest {
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final UserDataAccess userAccess = new UserDataAccess();
    private final GameDataAccess gameAccess = new GameDataAccess();
    private final ClearAll clearer = new ClearAll(userAccess, authAccess, gameAccess);
    private final GettingStarted starter = new GettingStarted(authAccess, userAccess);
    private final IsLoggedIn logged = new IsLoggedIn(authAccess, userAccess, gameAccess);
    private final UserData userFirst = new UserData("ngehring", "password", "hello@gmail.com");
    private final UserData userNext = new UserData("jgehring", "password1", "hello1@gmail.com");
    private RegisterResult user1 = null;
    private RegisterResult user2 = null;

    @BeforeEach
    public void fillItUp() throws DataAccessException, DoesNotExistException, DoesNotMatchException, AlreadyTakenException {
       user1 = starter.register(userFirst);
       user2 = starter.register(userNext);
       logged.create(user1.authToken(), new CreateRequest("game1"));
       logged.create(user1.authToken(), new CreateRequest("game2"));
       logged.create(user1.authToken(), new CreateRequest("game3"));
       logged.create(user2.authToken(), new CreateRequest("game4"));
       logged.create(user2.authToken(), new CreateRequest("game4"));
    }

    @Test
    void clearPositive(){
        clearer.clear();
        Assertions.assertNull(authAccess.getAuth(user1.authToken()));
        Assertions.assertNull(authAccess.getAuth(user2.authToken()));
        Assertions.assertThrows(DoesNotExistException.class,
                ()->{logged.list(user1.authToken());});
    }
}