package dataaccess;

import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import server.DataAccessException;

class SQLUserDataAccessTest {
    private final UserData user = new UserData("username1", "password1", "email1");
    private final UserData user2 = new UserData("user2", "password2", "email2");
    private final UserData nullUser = new UserData(null, null, null);
    private static UserInterface userAccess;

    @BeforeAll
    public static void setup() throws Exception {
        userAccess = new SQLUserDataAccess();
    }

    @BeforeEach
    public void clearAll() throws DataAccessException {
        try {
            userAccess.clear();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserPositive() throws DataAccessException, IllegalAccessException{
        userAccess.createUser(user);
        Assertions.assertTrue(BCrypt.checkpw(user.password(),
                userAccess.getUser(user.username()).password()));
    }

    @Test
    void createUserNegative(){
        Assertions.assertThrows(DataAccessException.class,
                ()-> userAccess.createUser(nullUser));
    }

    @Test
    void getUserPositive() throws DataAccessException, IllegalAccessException{
        userAccess.createUser(user2);
        Assertions.assertEquals(user2.email(), userAccess.getUser(user2.username()).email());
    }

    @Test
    void getUserNegative() throws DataAccessException, IllegalAccessException{
        Assertions.assertNull(userAccess.getUser("doesNotExist"));
    }

    @Test
    void clear() throws DataAccessException, IllegalAccessException{
        userAccess.createUser(user);
        userAccess.createUser(user2);
        try {
            userAccess.clear();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(userAccess.getUser(user.username()));
    }
}