package service;

import model.chessrecords.LoginRequest;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;

public class GettingStarted {
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;

    public GettingStarted(AuthDataAccess authAccess, UserDataAccess userAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }

    public RegisterResult register(UserData user) throws AlreadyTakenException, DataAccessException{
        if (user.username() == null || user.password() == null || user.email() == null){
            throw new DataAccessException("This is an invalid request");
        }

        if (userAccess.getUser(user.username()) == null){
            userAccess.create_user(user);
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        throw new AlreadyTakenException("This username is already taken");
    }
    public RegisterResult login(LoginRequest request) throws DoesNotExistException, DoesNotMatchException, DataAccessException{
        if(request.password() == null || request.username() == null){
            throw new DataAccessException("This request is invalid");
        }
        UserData user = userAccess.getUser(request.username());
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
