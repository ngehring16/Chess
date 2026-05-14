package dataaccess;

import chess.ChessGame;
import chessrecords.GameData;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void updategame(){

    }
}
