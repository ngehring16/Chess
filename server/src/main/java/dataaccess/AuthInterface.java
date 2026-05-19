package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import server.DataAccessException;

public interface AuthInterface {
    public String createAuth(UserData user) throws DataAccessException;
    public void deleteAuth(AuthData auth);
    public AuthData getAuth(String authToken);
    public void clear();
}
