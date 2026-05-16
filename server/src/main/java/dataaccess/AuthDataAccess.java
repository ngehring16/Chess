package dataaccess;
import java.util.HashMap;
import java.util.UUID;

import model.chessrecords.AuthData;
import model.chessrecords.UserData;

public class AuthDataAccess implements AuthInterface {
    private final HashMap<String, AuthData> authStorage = new HashMap<>();
    public AuthDataAccess(){}

    public String createAuth(UserData user){
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authStorage.put(authToken, auth);
        return authToken;
    }

    public void deleteAuth(AuthData auth){
        authStorage.remove(auth.authToken());
    }

    public AuthData getAuth(String authToken){
        return authStorage.get(authToken);
    }

    public void clear(){
        authStorage.clear();
    }

}
