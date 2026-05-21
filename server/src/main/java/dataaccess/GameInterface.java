package dataaccess;

import chess.ChessGame;
import model.chessrecords.GameData;
import server.DataAccessException;

import java.util.ArrayList;

public interface GameInterface {
    public GameData createGame(String gameName) throws DataAccessException;
    public GameData getGame(int id) throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;
    public void updateGame(int gameID, String whiteUsername,
                           String blackUsername, String gameName, ChessGame game) throws DataAccessException;
    public void clear() throws DataAccessException;
}
