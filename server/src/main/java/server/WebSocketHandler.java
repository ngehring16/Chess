package server;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthInterface;
import dataaccess.GameInterface;
import dataaccess.UserInterface;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthInterface authAccess;
    private final UserInterface userAccess;
    private final GameInterface gameAccess;
    private final WebSocketService service;
    public WebSocketHandler(AuthInterface authAccess, UserInterface userAccess, GameInterface gameAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
        service = new WebSocketService(connections, authAccess, userAccess, gameAccess);
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) throws Exception{
        UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        try{
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case MAKE_MOVE -> makeMove(userGameCommand.getGameID(), userGameCommand.getAuthToken(), userGameCommand.getMove(), ctx.session);
                case LEAVE -> leave(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case RESIGN -> resign(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
            }
        }
        catch (Exception E){
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, E.getMessage());
            String errorMessage = new Gson().toJson(error);
            ctx.session.getRemote().sendString(errorMessage);
        }

    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(int gameID, String authToken, Session session) throws Exception{
        service.connect(session, gameID, authToken);
    }

    private void makeMove(int gameID, String authToken, ChessMove move, Session session) throws Exception{
        service.makeMove(gameID, authToken, move, session);
    }

    private void leave(int gameID, String authToken, Session session){

    }

    private void resign(int gameID, String authToken, Session session){

    }
}
