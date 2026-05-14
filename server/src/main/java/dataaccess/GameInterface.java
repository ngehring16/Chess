package dataaccess;

import chess.ChessGame;
import chessrecords.GameData;

import java.util.ArrayList;

public interface GameInterface {
    public void createGame(String gameName);
    public GameData getGame(int id);
    public ArrayList<GameData> listGames();
    public void updategame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);
}
