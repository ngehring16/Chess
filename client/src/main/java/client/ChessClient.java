package client;

import exception.ResponseException;

public class ChessClient{
    private final ServerFacade server;
    public ChessClient(String url) throws ResponseException{
        server = new ServerFacade(url);
    }
    public void run(){
        PreLogin preLogin = new PreLogin(server);
        preLogin.run();
    }
}
