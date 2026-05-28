package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.chessrecords.DataAccessException;
import model.chessrecords.DoesNotExistException;

class SQLAuthDataAccessTest {
    private final  UserData user = new UserData("username1", "password1", "email1");
    private final UserData user2 = new UserData("user2", "password2", "email2");
    private final UserData nullUser = new UserData(null, null, null);
    private static AuthInterface authAccess;

    @BeforeAll
    public static void setup() throws Exception{
        authAccess = new SQLAuthDataAccess();
    }

    @BeforeEach
    public void clearAll() throws Exception{
       authAccess.clear();
    }

    @Test
    void createAuthPositive() throws Exception {
        String authToken = authAccess.createAuth(user);
        Assertions.assertEquals(authToken, authAccess.getAuth(authToken).authToken());
    }

    @Test
    void createAuthNegative(){
        Assertions.assertThrows(DataAccessException.class,
                ()-> authAccess.createAuth(nullUser));

    }

    @Test
    void deleteAuthPositive() throws Exception{
        String authToken2 = authAccess.createAuth(user2);
        AuthData authStored2 = authAccess.getAuth(authToken2);
        authAccess.deleteAuth(authStored2);
        Assertions.assertThrows(DoesNotExistException.class, ()-> authAccess.getAuth(authToken2));
    }

    @Test
    void deleteAuthNegative() throws Exception{
        String authToken2 = authAccess.createAuth(user2);
        authAccess.deleteAuth(new AuthData("LOLOLOLOLOLOLOL", "67lol"));
        Assertions.assertEquals(authToken2, authAccess.getAuth(authToken2).authToken());
    }

    @Test
    void getAuthPositive() throws Exception{
        String authToken = authAccess.createAuth(user);
        String authToken2 = authAccess.createAuth(user2);
        AuthData authStored1 = authAccess.getAuth(authToken);
        AuthData authStored2 = authAccess.getAuth(authToken2);
        Assertions.assertEquals(user.username(), authStored1.username());
        Assertions.assertEquals(user2.username(), authStored2.username());

    }

    @Test
    void getAuthNegative(){
        Assertions.assertThrows(DoesNotExistException.class,
                ()->authAccess.getAuth("falseTokenLOL"));
    }

    @Test
    void clear() throws Exception {
        String authToken = authAccess.createAuth(user);
        String authToken2 = authAccess.createAuth(user2);
        authAccess.clear();
        Assertions.assertThrows(DoesNotExistException.class, ()->authAccess.getAuth(authToken));
    }
}