package dataaccess;
import java.util.HashMap;
import java.util.UUID;

import chessrecords.AuthData;
import chessrecords.UserData;

public class AuthDataAccess implements AuthInterface {
    private final HashMap<String, AuthData> authStorage = new HashMap<>();
    public AuthDataAccess(){}

    public void createAuth(UserData user){
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authStorage.put(authToken, auth);
    }

    public void deleteAuth(AuthData auth){
        authStorage.remove(auth.authToken());
    }

    public AuthData getAuth(String authToken){
        return authStorage.get(authToken);
    }

}
