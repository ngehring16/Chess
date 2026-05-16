package dataaccess;

import chess.ChessGame;
import model.chessrecords.GameData;

import java.util.ArrayList;

public class GameDataAccess implements GameInterface {
    private final ArrayList<GameData> gameStorage = new ArrayList<>();
    private int gameIDSource = 10;
    public GameDataAccess(){}

    public GameData createGame(String gameName){
        GameData new_game = new GameData(gameIDSource, null, null, gameName, new ChessGame());
        gameStorage.addLast(new_game);
        gameIDSource++;
        return new_game;
    }

    public GameData getGame(int id){
        for (GameData gameData : gameStorage) {
            if (gameData.gameID() == id) {
                return gameData;
            }
        }
        return null;
    }

    public ArrayList<GameData> listGames(){
        return gameStorage;
    }

    public void updategame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        int index = 0;
        for (int i = 0; i < gameStorage.size(); i++){
            if (gameStorage.get(i).gameID() == gameID){
                index+= i;
                gameStorage.remove(i);
                break;
            }
        }
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameStorage.add(index, updatedGame);
    }

    public void clear(){
        gameStorage.clear();
    }
}
