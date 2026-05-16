package dataaccess;

import model.chessrecords.UserData;

public interface UserInterface {
    public void createUser(UserData user);
    public UserData getUser(String username);
    public void clear();
}
