package dataaccess;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;
import server.DataAccessException;

public interface AuthInterface {
    public String createAuth(UserData user) throws Exception;
    public void deleteAuth(AuthData auth) throws Exception;
    public AuthData getAuth(String authToken) throws Exception;
    public void clear() throws Exception;
}
