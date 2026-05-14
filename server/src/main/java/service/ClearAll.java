package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class ClearAll {
    private final UserDataAccess userAccess = new UserDataAccess();
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final GameDataAccess gameAccess = new GameDataAccess();
    public ClearAll(){}

    public void clear(){
        userAccess.clear();
        authAccess.clear();
        gameAccess.clear();
    }
}
