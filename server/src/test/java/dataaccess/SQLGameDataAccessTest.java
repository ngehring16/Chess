package dataaccess;

import chess.ChessGame;
import model.chessrecords.GameData;
import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDataAccessTest {
    private final GameData game1 = new GameData(10, null, null, "game1", new ChessGame());
    private static GameInterface gameAccess;
    @BeforeAll
    public static void setup() throws DataAccessException {
        gameAccess = new SQLGameDataAccess();
    }

    @BeforeEach
    public void clearAll() throws DataAccessException {
        gameAccess.clear();
    }
    @Test
    void createGamePositive() throws DataAccessException{
        GameData game = gameAccess.createGame("game1");
        Assertions.assertEquals(game1, game);
    }

    @Test
    void createGameNegative() {
        Assertions.assertThrows(DataAccessException.class,
                ()-> gameAccess.createGame(null));
    }

    @Test
    void getGamePositive() throws DataAccessException{
        GameData game = gameAccess.createGame("game1");
        Assertions.assertEquals(game, gameAccess.getGame(game.gameID()));
    }

    @Test
    void getGameNegative(){
        Assertions.assertThrows(DataAccessException.class,
                ()-> gameAccess.getGame(10));
    }

    @Test
    void listGames() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void clear() {
    }
}