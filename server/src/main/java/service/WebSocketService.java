package service;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthInterface;
import dataaccess.GameInterface;
import dataaccess.UserInterface;
import model.chessrecords.AuthData;
import model.chessrecords.DataAccessException;
import model.chessrecords.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.ConnectionManager;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;

public class WebSocketService {
    private final ConnectionManager connections;
    private final AuthInterface authAccess;
    private final UserInterface userAccess;
    private final GameInterface gameAccess;
    public WebSocketService(ConnectionManager connections, AuthInterface authAccess, UserInterface userAccess, GameInterface gameAccess){
        this.connections = connections;
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public void connect(Session session, int gameID, String authToken) throws Exception {
        isValid(authToken, gameID);
        connections.add(session, gameID);
        String username = getUsername(authToken);
        ChessGame.TeamColor teamColor =getTeam(username, gameID);
        String notify;
        if (teamColor == null){
            notify = username + " has joined the game to observe.";
        }
        else{
            notify = username + " has joined the game as " + teamColor;
        }
        ServerMessage loadGame = new ServerMessage(gameAccess.getGame(gameID).game());
        String jsonMessage = new Gson().toJson(loadGame);
        session.getRemote().sendString(jsonMessage);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notify);
        connections.broadcast(session, notification, gameID);
    }

    public void makeMove(int gameID, String authToken, ChessMove move, Session session) throws Exception{
        isValid(authToken, gameID);
        String username = getUsername(authToken);
        GameData gameData = gameAccess.getGame(gameID);
        ChessGame game = gameData.game();
        ChessGame.TeamColor teamColor =getTeam(username, gameID);
        if (game.getTeamTurn() != teamColor){
            throw new DataAccessException("It is not your turn! You cannot make moves.");
        }
        if (game.getGameState() == State.GAMEOVER){
            throw new DataAccessException("This game has ended. Please leave and choose a different game to play.");
        }
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
        if (!moveIsValid(validMoves, move)){
            throw new DataAccessException("This move is invalid. Please choose a valid move!");
        }
        ChessPiece.PieceType pieceType = game.getBoard().getPiece(move.getStartPosition()).getPieceType();
        game.makeMove(move);
        gameAccess.updateGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        ServerMessage loadGame = new ServerMessage(game);
        connections.broadcast(null, loadGame, gameID);
        String notify = username + " moved their " +
                pieceType + " to " + moveTranslator(move);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notify);
        connections.broadcast(session, notification, gameID);
        checkConditions(gameData, ChessGame.TeamColor.WHITE);
        checkConditions(gameData, ChessGame.TeamColor.BLACK);
    }

    public void leave(int gameID, String authToken, Session session) throws Exception{
        isValid(authToken, gameID);
        GameData gameData = gameAccess.getGame(gameID);
        String username = getUsername(authToken);
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)){
            gameAccess.updateGame(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)){
            gameAccess.updateGame(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        }
        ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game.");
        connections.remove(session, gameID);
        connections.broadcast(session, notify, gameID);
    }

    public void resign(int gameID, String authToken, Session session) throws Exception{
        isValid(authToken, gameID);
        GameData gameData = gameAccess.getGame(gameID);
        String username = getUsername(authToken);
        if (gameData.game().getGameState() == State.GAMEOVER){
            throw new DataAccessException("Please select leave in order to stop watching.");
        }
        if (getTeam(username, gameID) == null){
            throw new DataAccessException("It appears you are not currently playing! Please select leave if you would like to stop watching the game.");
        }
        gameData.game().setGameState(State.GAMEOVER);
        gameData.game().setBoard(new ChessBoard());
        gameAccess.updateGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned");
        connections.broadcast(null, notify, gameID);
    }


    private ChessGame.TeamColor getTeam(String username, int gameID) throws Exception{
        ChessGame.TeamColor teamColor;
        if (username.equals(gameAccess.getGame(gameID).whiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if(username.equals(gameAccess.getGame(gameID).blackUsername())){
            teamColor = ChessGame.TeamColor.BLACK;
        }
        else{
            teamColor = null;
        }
        return teamColor;
    }


    private String getUsername(String authToken) throws Exception{
        AuthData auth = authAccess.getAuth(authToken);
        return auth.username();
    }

    private String moveTranslator(ChessMove move){
        String[] letters = { "A", "B", "C" ,"D", "E", "F", "G", "H"};
        int row = move.getEndPosition().getRow();
        int col = move.getEndPosition().getColumn();
        return letters[col-1] + row;
    }

    private void isValid(String authToken, int gameID) throws Exception{
        if (authAccess.getAuth(authToken) == null){
            throw new DataAccessException("You are not authorized, please login.");
        }
        if (gameAccess.getGame(gameID) == null){
            throw new DataAccessException("This game does not exist. Please pick a different game.");
        }
    }

    private boolean moveIsValid(Collection<ChessMove> validMoves, ChessMove move){
        return validMoves.contains(move);
    }

    private void checkConditions(GameData gameData, ChessGame.TeamColor teamColor) throws Exception{
        String username;
        ChessGame game = gameData.game();
        if (teamColor == ChessGame.TeamColor.BLACK){
            username = gameData.blackUsername();
        }
        else{
           username = gameData.whiteUsername();
        }
        if(game.isInCheckmate(teamColor)){
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    "CHECKMATE! " + username + " has lost.");
            connections.broadcast(null, notify, gameData.gameID());
            game.setGameState(State.GAMEOVER);
        }
        else if(game.isInStalemate(teamColor)){
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    "STALEMATE! " + username + " is not in check but has no valid moves.");
            connections.broadcast(null, notify, gameData.gameID());
            game.setGameState(State.GAMEOVER);
        }
        else if(game.isInCheck(teamColor)){
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username + " is in check!");
            connections.broadcast(null, notify, gameData.gameID());
        }
    }

}
