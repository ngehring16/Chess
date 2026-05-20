package dataaccess;

import model.chessrecords.UserData;
import server.DataAccessException;

public interface UserInterface {
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void clear() throws DataAccessException;
}
