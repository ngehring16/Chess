package service;

import dataaccess.*;

public class ClearAll {
    private final UserInterface userAccess;
    private final AuthInterface authAccess;
    private final GameInterface gameAccess;
    public ClearAll(UserInterface userAccess, AuthInterface authAccess, GameInterface gameAccess){
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
