package server;

import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx){
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case MAKE_MOVE -> makeMove(userGameCommand.getGameID(), userGameCommand.getAuthToken(), MakeMoveCommand.getMove(), ctx.session);
                case LEAVE -> leave(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case RESIGN -> resign(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                default -> throw new IOException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(int gameID, String authToken, Session session){

    }

    private void makeMove(int gameID, String authToken, ChessMove move, Session session){

    }

    private void leave(int gameID, String authToken, Session session){

    }

    private void resign(int gameID, String authToken, Session session){

    }
}
