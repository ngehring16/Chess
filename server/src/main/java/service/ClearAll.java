package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class ClearAll {
    private final UserDataAccess userAccess;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;
    public ClearAll(UserDataAccess userAccess, AuthDataAccess authAccess, GameDataAccess gameAccess){
        this.userAccess = userAccess;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public void clear(){
        userAccess.clear();
        authAccess.clear();
        gameAccess.clear();
    }
}
