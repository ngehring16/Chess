package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.chessrecords.GameData;
import model.chessrecords.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameInterface{

    private int gameIDSource = 10;

    public SQLGameDataAccess() throws DataAccessException, IllegalAccessException{
        configureDatabase();
        gameIDSource += get_size();
    }

    private int get_size() throws DataAccessException, IllegalAccessException{
        int counter = 0;
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameStorage";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()){
                        counter ++;
                    }
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to create new game: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
        return counter;
    }

    public GameData createGame(String gameName) throws DataAccessException, IllegalAccessException {
        var game = new ChessGame();
        var statement = "INSERT INTO gameStorage (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                String newGame = new Gson().toJson(game);
                preparedStatement.setInt(1, gameIDSource);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, newGame);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to create new game: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
        GameData gameData = new GameData(gameIDSource, null, null, gameName, game);
        gameIDSource++;
        return gameData;
    }

    public GameData getGame(int id) throws DataAccessException, IllegalAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameStorage WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()){
                        var json = rs.getString("game");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), game);
                    }
                    throw new DataAccessException("This gameID is invalid");
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to retrieve authData: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            if (dae.getMessage().equals("This gameID is invalid")) {
                throw dae;
            }
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }

    public ArrayList<GameData> listGames() throws DataAccessException, IllegalAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameStorage";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString("game");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);
                        games.add(new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), game));

                    }
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to retrieve authData: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
        return games;
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException, IllegalAccessException {
        var statement = "UPDATE gameStorage SET gameID=?, whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID =?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                if (gameID < 10){
                    throw new DataAccessException("This gameID is invalid");
                }
                String newGame = new Gson().toJson(game);
                preparedStatement.setInt(6, gameID);
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, whiteUsername);
                preparedStatement.setString(3, blackUsername);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, newGame);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to update gameStorage: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            if (dae.getMessage().equals("This gameID is invalid")) {
                throw dae;
            }
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }

    }

    public void clear() throws DataAccessException, IllegalAccessException {
        gameIDSource = 10;
        var statement = "TRUNCATE TABLE gameStorage";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate(statement);
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to Delete Database: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }

    private void configureDatabase() throws DataAccessException, IllegalAccessException {
        String createStatement =  """
            CREATE TABLE IF NOT EXISTS gameStorage (
            `gameID` int NOT NULL,
            `whiteUsername` VARCHAR(256),
            `blackUsername` VARCHAR(256),
            `gameName` VARCHAR(256) NOT NULL,
            `game` TEXT DEFAULT NULL,
            PRIMARY KEY (`gameID`)
            )
            """;
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }
}
