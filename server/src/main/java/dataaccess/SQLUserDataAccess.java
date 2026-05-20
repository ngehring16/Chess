package dataaccess;

import model.chessrecords.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserInterface{
    public SQLUserDataAccess() throws DataAccessException{
        configureDatabase();
    }

    public void createUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        var statement = "INSERT INTO userStorage (username, password, email) VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to add to userStorage: %s", ex.getMessage()));
        }
    }

    public UserData getUser(String username) {
        return null;
    }

    public void clear() {

    }

    private void configureDatabase() throws DataAccessException {
        String createStatement =  """
            CREATE TABLE IF NOT EXISTS userStorage (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`password`)
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
