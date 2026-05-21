package dataaccess;

import model.chessrecords.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserInterface{
    public SQLUserDataAccess() throws Exception{
        configureDatabase();
    }

    private void connect(String statement) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }

    public void createUser(UserData user) throws DataAccessException, IllegalAccessException {
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
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }

    public UserData getUser(String username) throws DataAccessException, IllegalAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userStorage WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if(rs.next()){
                        return new UserData(rs.getString("username"),
                                rs.getString("password"), rs.getString("email"));
                    }
                    return null;
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to retrieve from userStorage: %s", ex.getMessage()));
        }
        catch (DataAccessException dae) {
            throw new IllegalAccessException("Failed to connect to the DataBase");
        }
    }
    public void clear() throws DataAccessException, IllegalAccessException {
        var statement = "TRUNCATE TABLE userStorage";
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

    private void configureDatabase() throws Exception {
        String createStatement =  """
            CREATE TABLE IF NOT EXISTS userStorage (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`)
            )
            """;
        DatabaseManager.createDatabase();
        connect(createStatement);
    }
}
