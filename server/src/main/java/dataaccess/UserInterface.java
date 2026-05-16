package dataaccess;

import model.chessrecords.UserData;

public interface UserInterface {
    public void create_user(UserData user);
    public UserData getUser(String username);
    public void clear();
}
