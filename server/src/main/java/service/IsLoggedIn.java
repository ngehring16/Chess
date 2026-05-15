package service;

import chessrecords.AuthData;
import chessrecords.DoesNotExistException;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class IsLoggedIn {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public IsLoggedIn(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public AuthData isAuthorized(String authToken){
        return authAccess.getAuth(authToken);
    }

    public void logout(String request) throws DoesNotExistException {
        AuthData auth = isAuthorized(request);
        if (auth == null){
            throw new DoesNotExistException("This authToken does not exist");
        }
        authAccess.deleteAuth(auth);
    }

    public void list(String request){}

    public void create(){}

    public void joinGame(){}
    }




