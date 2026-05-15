package dataaccess;

import chess.ChessGame;
import chessrecords.GameData;

import java.util.ArrayList;

public class GameDataAccess implements GameInterface {
    private final ArrayList<GameData> gameStorage = new ArrayList<>();
    private int idCounter = 0;
    public GameDataAccess(){}

    public GameData createGame(String gameName){
        GameData new_game = new GameData(idCounter + 1000, null, null, gameName, new ChessGame());
        gameStorage.add(idCounter, new_game);
        idCounter++;
        return new_game;
    }

    public GameData getGame(int id){
        return gameStorage.get(id-1000);
    }

    public ArrayList<GameData> listGames(){
        return gameStorage;
    }

    public void updategame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        gameStorage.remove(gameID-1000);
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameStorage.add(gameID-1000, updatedGame);
    }

    public void clear(){
        gameStorage.clear();
    }
}
