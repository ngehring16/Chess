package service;

import dataaccess.AuthInterface;
import dataaccess.UserInterface;
import model.chessrecords.LoginRequest;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.AlreadyTakenException;
import server.DataAccessException;
import server.DoesNotExistException;
import server.DoesNotMatchException;

public class GettingStarted {
    private final AuthInterface authAccess;
    private final UserInterface userAccess;

    public GettingStarted(AuthInterface authAccess, UserInterface userAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }

    public RegisterResult register(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null){
            throw new DataAccessException("This is an invalid request");
        }

        if (userAccess.getUser(user.username()) == null){
            userAccess.createUser(user);
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        throw new AlreadyTakenException();
    }
    public RegisterResult login(LoginRequest request) throws Exception{
        if(request.password() == null || request.username() == null){
            throw new DataAccessException("This request is invalid");
        }
        UserData user = userAccess.getUser(request.username());
        if (user == null){
            throw new DoesNotExistException();
        }
        if (BCrypt.checkpw(request.password(), user.password())){
            String authToken = authAccess.createAuth(user);
            return new RegisterResult(user.username(), authToken);
        }
        else{
            throw new DoesNotMatchException();
        }
    }

}
