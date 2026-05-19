package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import server.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDataAccess implements AuthInterface{

    public SQLAuthDataAccess() throws DataAccessException{
        configureDatabase();
    }

    public String createAuth(UserData user) throws DataAccessException{
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
        return authToken;
    }
    public void deleteAuth(AuthData auth){}
    public AuthData getAuth(String authToken){
        return null;
    }
    public void clear(){}

    private final String createStatement =
            """
            CREATE TABLE IF NOT EXISTS authStorage (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`)
            )
            """;

    private void configureDatabase() throws DataAccessException {
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
