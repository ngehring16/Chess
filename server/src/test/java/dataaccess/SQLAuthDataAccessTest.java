package dataaccess;

import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDataAccessTest {

    @Test
    void createAuth() throws DataAccessException {
        UserData user = new UserData("username1", "password1", "email1");
        SQLAuthDataAccess authAccess = new SQLAuthDataAccess();
        String authToken = authAccess.createAuth(user);
        Assertions.assertEquals(authToken, authAccess.getAuth(authToken).authToken());
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void getAuth() {
    }

    @Test
    void clear() {
    }
}