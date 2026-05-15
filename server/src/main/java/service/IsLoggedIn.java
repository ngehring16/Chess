package service;

import chessrecords.AuthData;
import chessrecords.DoesNotExistException;
import chessrecords.LogoutRequest;
import dataaccess.AuthDataAccess;

public class IsLoggedIn {
    private final AuthDataAccess authAccess = new AuthDataAccess();

    public IsLoggedIn(){}

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

    public void list(){}

    public void create(){}

    public void joinGame(){}
    }




