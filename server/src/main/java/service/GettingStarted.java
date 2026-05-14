package service;

import chessrecords.RegisterResult;
import chessrecords.UserData;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;

public class GettingStarted {
    private final UserDataAccess dataAccess = new UserDataAccess();
    private final AuthDataAccess authAccess = new AuthDataAccess();
    public GettingStarted(){}

    public RegisterResult register(UserData user) throws AlreadyTakenException{
        if (dataAccess.getUser(user.username()) == null){
            dataAccess.create_user(user);
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        throw new AlreadyTakenException("This username is already taken");
    }

}
