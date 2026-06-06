package client;

import WebSocket.NotificationManager;
import WebSocket.WebSocketFacade;
import chess.*;
import exception.ResponseException;
import model.chessrecords.GameData;
import websocket.messages.ServerMessage;
import static ui.EscapeSequences.*;

import java.util.Scanner;

public class Gameplay extends LoopTools implements NotificationManager {
    private final GameData gameData;
    private final ChessGame.TeamColor teamColor;
    private final String authToken;
    private final ChessMove nullMove = new ChessMove(new ChessPosition(0,0), new ChessPosition(0,0), null);
    private ChessMove currentMove = nullMove;

    private final WebSocketFacade facade;
    public Gameplay(GameData gameData, ChessGame.TeamColor teamColor, String url, String authToken){
        this.gameData = gameData;
        this.teamColor = teamColor;
        this.facade = new WebSocketFacade(url, this);
        this.authToken = authToken;
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
        gameData.game().setGameState(State.GAMEON);
        try{
            facade.connect(authToken, gameData.gameID());
        } catch (Exception e) {
            errorFormat("ERROR: there was an unexpected issue. Please try again.");
            return;
        }
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
                case "leave" -> leave();
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

    public String leave(){
        facade.leave(authToken, gameData.gameID());
        return "quit";
    }

    public String redrawBoard(){
        DrawBoard drawBoard = new DrawBoard(gameData.game(), teamColor);
        drawBoard.run(currentMove);
        return "";
    }

    public String makeMove(){
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.println("Move(RowCol): ");
        ChessMove move = moveGetter();
        System.out.print(RESET_TEXT_COLOR);
        facade.makeMove(authToken, gameData.gameID(), move);
        currentMove = move;
        return "";
    }

    public String resign(){
        facade.resign(authToken, gameData.gameID());
        return "";
    }

    public String highlightLegalMoves(){return "";}

    private void displayNotification(ServerMessage notification){
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println(notification.getMessage());
        System.out.print(RESET_TEXT_COLOR);
    }

    private void displayError(ServerMessage message){
        errorFormat(message.getErrorMessage());
    }

    private void loadGame(ServerMessage game){
        DrawBoard drawBoard = new DrawBoard(game.getGame(), teamColor);
        drawBoard.run(currentMove);
    }

    private ChessMove moveGetter(){
        String[] letters = { "A", "B", "C" ,"D", "E", "F", "G", "H"};
        String input1 = null;
        String input2 = null;
        ChessPiece.PieceType promotionPiece = null;
        int col1 = 0;
        int row1 = 0;
        int col2 = 0;
        int row2 = 0;
        ChessPosition startPosition = getPosition(letters, row1, col1, input1, "START: ");
        ChessPosition endPosition = getPosition(letters, row2, col2, input2, "END: ");
        if (gameData.game().getBoard().getPiece(startPosition) == null){
            errorFormat("ERROR: Please select a move with a valid piece.");
            startPosition = getPosition(letters, row1, col1, input1, "START: ");
            endPosition = getPosition(letters, row2, col2, input2, "END: ");
        }
        ChessPiece piece = gameData.game().getBoard().getPiece(startPosition);
        if (piece.getTeamColor() != teamColor){
            errorFormat("ERROR: Please select a move with a valid piece.");
            startPosition = getPosition(letters, row1, col1, input1, "START: ");
            endPosition = getPosition(letters, row2, col2, input2, "END: ");
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8){
            promotionPiece = getPromotionPiece(promotionPiece);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1){
            promotionPiece = getPromotionPiece(promotionPiece);
        }
        return new ChessMove(startPosition, endPosition, promotionPiece);
    }

    private void errorFormat(String message){
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(message);
        System.out.print(RESET_TEXT_COLOR);
    }

    private ChessPosition getPosition(String[] letters, int row, int col, String input, String query){
        while (input == null){
            System.out.print(SET_TEXT_COLOR_YELLOW);
            input = getSingleInput(query);
            String[] spot = input.split("");
            for (int i = 0; i < 9; i++){
                if (spot[0].equals(letters[i].toLowerCase())){
                    col = i+1;
                    break;
                }
                if (i == 8 && col == 0){
                    errorFormat("ERROR: Please input a valid position in this format- A4");
                    break;
                }
            }
            try{
                row = Integer.parseInt(spot[1]);
            } catch (NumberFormatException e) {
                errorFormat("ERROR: Please input a valid position in this format- A4");
                continue;
            }
            if (row < 0 || row > 8 || col <0 || col > 8){
                errorFormat("ERROR: Please input a valid position in this format- A4");
                continue;
            }
        }
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType getPromotionPiece(ChessPiece.PieceType promotionPiece){
        String promotion = null;
        while (promotion == null){
            System.out.print(SET_TEXT_COLOR_YELLOW);
            promotion = getSingleInput("Promotion type: <ROOK, KNIGHT, QUEEN, BISHOP>");
            String[] words = promotion.toLowerCase().split(" ");
            String command = words[0];
            switch (command) {
                case "rook" -> promotionPiece = ChessPiece.PieceType.ROOK;
                case "knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                case "queen" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case "bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                default -> {
                    errorFormat("ERROR: Please select a valid promotion piece.");
                    promotion = null;
                }
            }
        }
        return promotionPiece;
    }
}
