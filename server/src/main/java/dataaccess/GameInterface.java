package dataaccess;

import chess.ChessGame;
import model.chessrecords.GameData;

import java.util.ArrayList;

public interface GameInterface {
    public GameData createGame(String gameName);
    public GameData getGame(int id);
    public ArrayList<GameData> listGames();
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);
    public void clear();
}
