package service;

import chess.ChessGame;
import model.chessrecords.*;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IsLoggedInTest {
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final UserDataAccess userAccess = new UserDataAccess();
    private final GameDataAccess gameAccess = new GameDataAccess();
    private final GettingStarted starter = new GettingStarted(authAccess, userAccess);
    private final IsLoggedIn logged = new IsLoggedIn(authAccess, userAccess, gameAccess);
    private final UserData user = new UserData("ngehring", "password", "hello@gmail.com");
    private RegisterResult result = null;

    @BeforeEach
    public void setup() throws AlreadyTakenException, DataAccessException{
        ClearAll clearer = new ClearAll(userAccess, authAccess, gameAccess);
        clearer.clear();
        result = starter.register(user);
    }

    @Test
    public void logoutPositiveAuthTokenIsDeleted() throws DoesNotExistException {
        logged.logout(result.authToken());
        Assertions.assertNull(authAccess.getAuth(result.authToken()));
    }

    @Test
    public void logoutNegativeBadToken(){
        Assertions.assertThrows(DoesNotExistException.class, ()->{logged.logout("hyyfkj_uebnaksu_poakbe");});
    }

    @Test
    void listPositiveSizeGrows() throws DataAccessException, DoesNotExistException{
        logged.create(result.authToken(), new CreateRequest("new_game"));
        logged.create(result.authToken(), new CreateRequest("newer_game"));
        ListResult result1 = logged.list(result.authToken());
        Assertions.assertEquals(2, result1.games().size());
    }

    @Test
    void listNegativeSizeStays()throws DoesNotExistException{
        Assertions.assertThrows(DataAccessException.class,
                ()->{logged.create(result.authToken(), new CreateRequest(null));});
        Assertions.assertThrows(DoesNotExistException.class,
                ()->{logged.create("my_authToken_is_valid", new CreateRequest("my_valid_game"));});
        ListResult result1 = logged.list(result.authToken());
        Assertions.assertEquals(0, result1.games().size());
    }

    @Test
    void createPositiveDifferentID() throws DataAccessException, DoesNotExistException {
        CreateResult game1 = logged.create(result.authToken(), new CreateRequest("game1"));
        CreateResult game2 = logged.create(result.authToken(), new CreateRequest("game2"));
        Assertions.assertNotEquals(game1.gameID(), game2.gameID());
    }

    @Test
    void createNegativeBadToken(){
        Assertions.assertThrows(DoesNotExistException.class,
                ()->{logged.create("fake_token", new CreateRequest("game1"));});
    }

    @Test
    void joinGamePositiveCorrectTeam() throws DataAccessException, DoesNotExistException, AlreadyTakenException{
        logged.create(result.authToken(), new CreateRequest("game1"));
        CreateResult game2 = logged.create(result.authToken(), new CreateRequest("game2"));
        logged.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, game2.gameID()), result.authToken());
        Assertions.assertEquals(user.username(), gameAccess.getGame(game2.gameID()).whiteUsername());
    }

    @Test
    void joinGameNegativeBadID(){
        Assertions.assertThrows(DataAccessException.class,
                ()->{logged.joinGame(new JoinRequest(ChessGame.TeamColor.BLACK, 45), result.authToken());});
    }
}