package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.chessrecords.GameData;
import model.chessrecords.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DataAccessException;

import java.util.ArrayList;

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
    void listGamesPositive() throws DataAccessException{
        gameAccess.createGame("game1");
        gameAccess.createGame("game2");
        gameAccess.createGame("game3");
        ArrayList<GameData> games = gameAccess.listGames();
        Assertions.assertEquals(3, games.size());
    }

    @Test
    void listGamesNegative() {
        Assertions.assertThrows(DataAccessException.class, ()-> gameAccess.listGames());
    }

    @Test
    void updateGamePositive() throws DataAccessException, InvalidMoveException {
        GameData game = gameAccess.createGame("game1");
        gameAccess.updateGame(10, "username1", null, game.gameName(),game.game());
        Assertions.assertEquals("username1", gameAccess.getGame(10).whiteUsername());
    }

    @Test
    void updateGameChangesBoardAfterMove() throws DataAccessException, InvalidMoveException {
        GameData game = gameAccess.createGame("game1");
        game.game().makeMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null));
        gameAccess.updateGame(10, "username1", null, game.gameName(),game.game());
        Assertions.assertEquals(game.game(), gameAccess.getGame(game.gameID()).game());
    }

    @Test
    void updateGameNegative() {
        Assertions.assertThrows(DataAccessException.class, ()->
                gameAccess.updateGame(0, null, null, null, null));
    }

    @Test
    void clear() {
    }
}