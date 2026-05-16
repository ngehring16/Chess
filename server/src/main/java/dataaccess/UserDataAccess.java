package dataaccess;

import model.chessrecords.UserData;

import java.util.HashMap;

public class UserDataAccess implements UserInterface{
    private final HashMap<String, UserData> userStorage = new HashMap<>();
    public UserDataAccess(){}

    public void createUser(UserData user){
        userStorage.put(user.username(), user);
    }

    public UserData getUser(String username){
        if (userStorage.get(username) == null){
            return null;
        }
        return userStorage.get(username);
    }
    public void clear(){
        userStorage.clear();
    }




}
