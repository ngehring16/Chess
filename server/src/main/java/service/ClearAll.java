package service;

import dataaccess.*;
import server.DataAccessException;

public class ClearAll {
    private final UserInterface userAccess;
    private final AuthInterface authAccess;
    private final GameInterface gameAccess;
    public ClearAll(UserInterface userAccess, AuthInterface authAccess, GameInterface gameAccess){
        this.userAccess = userAccess;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public void clear() throws Exception {
        try {
            userAccess.clear();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        authAccess.clear();
        gameAccess.clear();
    }
}
