package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationManager notificationManager;
    public WebSocketFacade(String url, NotificationManager notificationManager) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationManager = notificationManager;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    notificationManager.notify(serverMessage);
                }
            });
        } catch (Exception e) {
            notificationManager.notify(new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }

    public void connect(String authToken, int gameID) throws Exception{
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new ResponseException(e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move){
        try{
            var command = new UserGameCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new ResponseException(e.getMessage());
        }
    }

    public void resign(String authToken, int gameID){
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new ResponseException(e.getMessage());
        }
    }

    public void leave(String authToken, int gameID){
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new ResponseException(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
