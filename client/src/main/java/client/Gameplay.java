package client;

import websocket.NotificationManager;
import websocket.WebSocketFacade;
import chess.*;
import exception.ResponseException;
import model.chessrecords.GameData;
import websocket.messages.ServerMessage;
import static ui.EscapeSequences.*;

import java.util.Scanner;

public class Gameplay extends LoopTools implements NotificationManager {
    private final GameData gameData;
    private ChessGame playGame;
    private final ChessGame.TeamColor teamColor;
    private final String authToken;
    private final String[] letters = { "A", "B", "C" ,"D", "E", "F", "G", "H"};

    private final WebSocketFacade facade;
    public Gameplay(GameData gameData, ChessGame.TeamColor teamColor, String url, String authToken){
        this.gameData = gameData;
        this.teamColor = teamColor;
        this.facade = new WebSocketFacade(url, this);
        playGame = gameData.game();
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
        playGame.setGameState(State.GAMEOPEN);
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
        DrawBoard drawBoard = new DrawBoard(playGame, teamColor);
        drawBoard.draw(null);
        return "";
    }

    public String makeMove(){
        if (playGame.getGameState() == State.GAMEOVER){
            errorFormat("This game has ended. Please leave and choose a different game to play.");
            return "";
        }
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.println("Move(RowCol): ");
        ChessMove move = moveGetter();
        System.out.print(RESET_TEXT_COLOR);
        facade.makeMove(authToken, gameData.gameID(), move);
        return "";
    }

    public String resign(){
        String input = null;
        while (input == null){
            System.out.print(SET_TEXT_COLOR_BLUE);
            System.out.println("Are you sure you would like to resign? This will end the game.");
            input = getSingleInput("Enter YES/NO");
            System.out.println(RESET_TEXT_COLOR);
            if (input == null){
                continue;
            }
            String[] words = input.toLowerCase().split(" ");
            String command = words[0];
            switch (command) {
                case "yes" -> facade.resign(authToken, gameData.gameID());
                case "no" -> {
                    System.out.print(SET_TEXT_COLOR_BLUE);
                    System.out.println("OK! Let's keep playing.");
                    System.out.print(RESET_TEXT_COLOR);
                    return "";
                }
                default -> {
                    errorFormat("ERROR: Please give a valid input.");
                    input = null;
                    continue;
                }
            }
        }
        return "";
    }

    public String highlightLegalMoves(){
        int row = 0;
        int col = 0;
        ChessPosition position = null;
        String input = null;
        System.out.print(SET_TEXT_COLOR_GREEN);
        while (position == null) {
            position = getPosition(letters, row, col, input, "Piece: ");
            if (playGame.getBoard().getPiece(position) == null) {
                errorFormat("ERROR: Please select a valid piece.");
                position = null;
                continue;
            }
            ChessPiece piece = playGame.getBoard().getPiece(position);
            if (piece.getTeamColor() != teamColor) {
                errorFormat("ERROR: Please select a valid piece.");
                position = null;
                continue;
            }
        }
        DrawBoard drawBoard = new DrawBoard(playGame, teamColor);
        drawBoard.draw(position);
        if (playGame.validMoves(position).isEmpty()){
            System.out.print(SET_TEXT_COLOR_BLUE);
            System.out.println("This piece has no legal moves!");
            System.out.print(RESET_TEXT_COLOR);
        }
        return "";
    }

    private void displayNotification(ServerMessage notification){
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println(notification.getMessage());
        System.out.print(RESET_TEXT_COLOR);
    }

    private void displayError(ServerMessage message){
        errorFormat(message.getErrorMessage());
    }

    private void loadGame(ServerMessage game){
        playGame = game.getGame();
        DrawBoard drawBoard = new DrawBoard(game.getGame(), teamColor);
        drawBoard.draw(null);
    }

    private ChessMove moveGetter(){
        String input1 = null;
        String input2 = null;
        ChessPiece.PieceType promotionPiece = null;
        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        ChessPiece piece = null;
        int col1 = 0;
        int row1 = 0;
        int col2 = 0;
        int row2 = 0;
        while (startPosition == null) {
            startPosition = getPosition(letters, row1, col1, input1, "START: ");
            if (playGame.getBoard().getPiece(startPosition) == null) {
                errorFormat("ERROR: Please select a move with a valid piece.");
                startPosition = null;
                continue;
            }
            piece = playGame.getBoard().getPiece(startPosition);
            if (piece.getTeamColor() != teamColor) {
                errorFormat("ERROR: Please select a move with a valid piece.");
                startPosition = null;
                continue;
            }
        }
        endPosition = getPosition(letters, row2, col2, input2, "END: ");
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {
            promotionPiece = getPromotionPiece(promotionPiece);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1) {
            promotionPiece = getPromotionPiece(promotionPiece);
        }
        return new ChessMove(startPosition, endPosition, promotionPiece);
    }

    private ChessPosition getPosition(String[] letters, int row, int col, String input, String query){
        while (input == null){
            System.out.print(SET_TEXT_COLOR_YELLOW);
            input = getSingleInput(query);
            if (input == null){
                continue;
            }
            String[] spot = input.split("");
            col = letterTranslator(letters, spot[0], col);
            if (col == 0){
                errorFormat("ERROR: Please input a valid position in this format- A4");
                input = null;
                continue;
            }
            try{
                row = Integer.parseInt(spot[1]);
            }
            catch (Exception e) {
                errorFormat("ERROR: Please input a valid position in this format- A4");
                input = null;
                continue;
            }
            if (row < 0 || row > 8 || col < 0 || col > 8){
                errorFormat("ERROR: Please input a valid position in this format- A4");
                input = null;
            }
        }
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType getPromotionPiece(ChessPiece.PieceType promotionPiece){
        String promotion = null;
        while (promotion == null){
            System.out.print(SET_TEXT_COLOR_YELLOW);
            System.out.println("Promotion:");
            promotion = getSingleInput("<ROOK"+ WHITE_ROOK +", KNIGHT"+ WHITE_KNIGHT +
                    ", QUEEN" + WHITE_QUEEN + ", BISHOP"+ WHITE_BISHOP+ ">");
            System.out.println(RESET_TEXT_COLOR);
            if (promotion == null){
                continue;
            }
            switch (promotion) {
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

    private int letterTranslator(String[] letters, String spot, int col){
        for (int i = 0; i < 8; i++){
            if (spot.equals(letters[i].toLowerCase())){
                col = i+1;
                break;
            }
        }
        return col;
    }
}
