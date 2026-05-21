package dataaccess;

import chess.ChessGame;
import model.chessrecords.GameData;
import server.DataAccessException;

import java.util.ArrayList;

public interface GameInterface {
    public GameData createGame(String gameName) throws DataAccessException, IllegalAccessException;
    public GameData getGame(int id) throws DataAccessException, IllegalAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException, IllegalAccessException;
    public void updateGame(int gameID, String whiteUsername,
                           String blackUsername, String gameName, ChessGame game) throws DataAccessException, IllegalAccessException;
    public void clear() throws DataAccessException, IllegalAccessException;
}
