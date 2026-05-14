package dataaccess;

import chessrecords.UserData;

import java.util.HashMap;

public class UserDataAccess implements UserInterface{
    private final HashMap<String, UserData> userStorage = new HashMap<>();
    public UserDataAccess(){}

    public void create_user(UserData user){
        userStorage.put(user.username(), user);
    }

    public UserData getUser(String username){
        return userStorage.get(username);
    }




}
