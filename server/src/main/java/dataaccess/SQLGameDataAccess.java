package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.chessrecords.AuthData;
import model.chessrecords.GameData;
import server.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameInterface{

    private int gameIDSource = 10;

    public SQLGameDataAccess() throws DataAccessException{
        configureDatabase();
    }

    public GameData createGame(String gameName) throws DataAccessException {
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
        GameData gameData = new GameData(gameIDSource, null, null, gameName, game);
        gameIDSource++;
        return gameData;
    }

    public GameData getGame(int id) throws DataAccessException {
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
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameStorage";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next() && !rs.getString("gamename").isEmpty()) {
                        var json = rs.getString("game");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);
                        games.add(new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), game));

                    }
                    if (games.isEmpty()){
                        throw new DataAccessException("This gameID is invalid");
                    }
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to retrieve authData: %s", ex.getMessage()));
        }
        return games;
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
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

    }

    public void clear() throws DataAccessException {
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
    }

    private void configureDatabase() throws DataAccessException {
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
    }
}
