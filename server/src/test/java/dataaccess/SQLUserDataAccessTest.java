package dataaccess;

import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import server.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDataAccessTest {
    private final UserData user = new UserData("username1", "password1", "email1");
    private final UserData user2 = new UserData("user2", "password2", "email2");
    private final UserData nullUser = new UserData(null, null, null);
    private static UserInterface userAccess;

    @BeforeAll
    public static void setup() throws DataAccessException {
        userAccess = new SQLUserDataAccess();
    }

    @BeforeEach
    public void clearAll() throws DataAccessException {
        userAccess.clear();
    }

    @Test
    void createUserPositive() throws DataAccessException{
        userAccess.createUser(user);
        Assertions.assertTrue(BCrypt.checkpw(user.password(), userAccess.getUser(user.username()).password()));
    }

    @Test
    void getUser() throws DataAccessException{
    }

    @Test
    void clear() throws DataAccessException{
    }
}