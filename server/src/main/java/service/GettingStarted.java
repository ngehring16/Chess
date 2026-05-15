package service;

import chessrecords.LoginRequest;
import chessrecords.RegisterResult;
import chessrecords.UserData;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;

public class GettingStarted {
    private final UserDataAccess dataAccess = new UserDataAccess();
    private final AuthDataAccess authAccess = new AuthDataAccess();
    public GettingStarted(){}

    public RegisterResult register(UserData user) throws AlreadyTakenException, DataAccessException{
        if (user.username() == null || user.password() == null || user.email() == null){
            throw new DataAccessException("This is an invalid request");
        }

        if (dataAccess.getUser(user.username()) == null){
            dataAccess.create_user(user);
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        throw new AlreadyTakenException("This username is already taken");
    }
    public RegisterResult login(LoginRequest request) throws DoesNotExistException, DoesNotMatchException, DataAccessException{
        if(request.password() == null || request.username() == null){
            throw new DataAccessException("This request is invalid");
        }
        UserData user = dataAccess.getUser(request.username());
        if (user == null){
            throw new DoesNotExistException("This username does not exist");
        }
        if (user.password().equals(request.password())){
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        else{
            throw new DoesNotMatchException("This username and password do not match");
        }
    }

}
