package client;

import WebSocket.NotificationManager;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.chessrecords.GameData;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Gameplay extends LoopTools implements NotificationManager {
    private final GameData gameData;
    private final ChessGame.TeamColor teamColor;
    public Gameplay(GameData gameData, ChessGame.TeamColor teamColor){
        this.gameData = gameData;
        this.teamColor = teamColor;
    }

    @Override
    public void notify(ServerMessage notification){
        switch(notification.getServerMessageType()){
            case NOTIFICATION -> displayNotification(notification);
            case LOAD_GAME ->  loadGame(notification);
            case ERROR -> displayError(notification);
        }
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";
        runLoop(result, help(), scanner);
    }

    public String eval(String input){
        try {
            String[] words = input.toLowerCase().split(" ");
            String command = (words.length > 0) ? words[0] : "help";
            return switch (command) {
                case "redraw" -> redrawBoard();
                case "leave" -> "quit";
                case "make" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves();
                default -> help();
            };
        }
        catch(ResponseException ex){
            throw new ResponseException(ex.getMessage());
        }

    }

    public String help(){
        return """
                
                GAMEPLAY:
                -HELP
                -REDRAW CHESSBOARD
                -LEAVE
                -MAKE MOVE
                -RESIGN
                -HIGHLIGHT LEGAL MOVES
                """;
    }

    public String redrawBoard(){
        DrawBoard drawBoard = new DrawBoard(gameData, teamColor);
        drawBoard.run(new ChessMove(new ChessPosition(2,2), new ChessPosition(3, 2), null));
        return "";
    }

    public String makeMove(){return "";}

    public String resign(){return "";}

    public String highlightLegalMoves(){return "";}

    private void displayNotification(ServerMessage notification){}

    private void displayError(ServerMessage message){}

    private void loadGame(ServerMessage game){}
}
