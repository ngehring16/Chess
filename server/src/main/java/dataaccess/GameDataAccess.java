package dataaccess;

import chess.ChessGame;
import chessrecords.GameData;

import java.util.ArrayList;

public class GameDataAccess implements GameInterface {
    private final ArrayList<GameData> gameStorage = new ArrayList<>();
    private int idCounter = 0;
    public GameDataAccess(){}

    public void createGame(String gameName){
        GameData new_game = new GameData(idCounter, null, null, gameName, new ChessGame());
        gameStorage.add(idCounter, new_game);
        idCounter++;
    }

    public GameData getGame(int id){
        return gameStorage.get(id);
    }

    public ArrayList<GameData> listGames(){
        return gameStorage;
    }

    public void updategame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        gameStorage.remove(gameID);
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameStorage.add(gameID, updatedGame);
    }

    public void clear(){
        gameStorage.clear();
    }
}
