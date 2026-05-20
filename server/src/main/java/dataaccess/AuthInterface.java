package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import server.DataAccessException;

public interface AuthInterface {
    public String createAuth(UserData user) throws DataAccessException;
    public void deleteAuth(AuthData auth) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
}
