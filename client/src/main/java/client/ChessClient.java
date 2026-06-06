package client;

import exception.ResponseException;

public class ChessClient{
    private final ServerFacade server;
    private final String url;
    public ChessClient(String url) throws ResponseException{
        server = new ServerFacade(url);
        this.url = url;
    }
    public void run(){
        PreLogin preLogin = new PreLogin(server, url);
        preLogin.run();
    }
}
