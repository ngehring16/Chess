package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import server.DataAccessException;
import server.DoesNotExistException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDataAccess implements AuthInterface{

    public SQLAuthDataAccess() throws DataAccessException, IllegalAccessException{
        configureDatabase();
    }

    public String createAuth(UserData user) throws DataAccessException, IllegalAccessException{
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO authStorage (authToken, username) VALUES (?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, user.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to add to authStorage: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
        return authToken;
    }
    public void deleteAuth(AuthData auth) throws DataAccessException, IllegalAccessException{
        var statement = "DELETE FROM authStorage WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to delete authData: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }
    public AuthData getAuth(String authToken) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authStorage WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()){
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                    throw new DoesNotExistException("This AuthToken is invalid");
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to retrieve authData: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            if (dae.getMessage().equals("This AuthToken is invalid")) {
                throw dae;
            }
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }
    public void clear() throws DataAccessException, IllegalAccessException{
        var statement = "TRUNCATE TABLE authStorage";
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

    private final String createStatement =
            """
            CREATE TABLE IF NOT EXISTS authStorage (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`)
            )
            """;

    private void configureDatabase() throws DataAccessException, IllegalAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessError("Failed to connect to the DataBase");
        }
    }
}
