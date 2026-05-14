package dataaccess;

import chessrecords.AuthData;
import chessrecords.UserData;

public interface AuthInterface {
    public void createAuth(UserData user);
    public void deleteAuth(AuthData auth);
    public AuthData getAuth(String authToken);
}
