package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.chessrecords.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private final ChessGame.TeamColor teamColor;
    private final ChessBoard board;

    public DrawBoard(GameData gameData, ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
        ChessGame game = gameData.game();
        board = game.getBoard();
    }

    public void run(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        if (teamColor == ChessGame.TeamColor.WHITE){
            drawWhite(out);
        }
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void drawWhite(PrintStream out){
        ChessPiece piece = null;
        String[] top = { "A", "B", "C" ,"D", "E", "F", "G", "H"};
        drawLetters(out, top);
        for (int j = 8; j > 0; j--){
            drawNumbers(out, j);
            for (int k = 1; k < 9; k++){
                drawRow(out, j, k, piece);
            }
            drawNumbers(out, j);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        drawLetters(out, top);

    }

    private void drawRow(PrintStream out, int j, int k, ChessPiece piece){
        if ((j+k)%2 == 0){
            out.print(SET_BG_COLOR_DARK_GREY);
        }
        else{
            out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        try{
            piece = board.getPiece(new ChessPosition(j, k));
        } catch (Exception e) {
            piece = null;
        }
        String input = pieceTranslator(piece);
        drawSquare(out, input);
    }

    private void drawNumbers(PrintStream out, int i){
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(" ");
        out.print(i);
        out.print(" ");
    }

    private void drawSquare(PrintStream out, String text){
        out.print(SET_TEXT_COLOR_WHITE);
        if (text.isEmpty()){
            out.print(EMPTY.repeat(1));
            return;
        }
        out.print(text);
    }

    private void drawLetters(PrintStream out, String[] text){
        out.print(SET_BG_COLOR_MAGENTA);
        out.print("  ");
        for (int i = 0; i < 8; i++){
            out.print(SET_BG_COLOR_MAGENTA);
            out.print(SET_TEXT_BOLD);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print("  ");
            out.print(text[i]);
        }
        out.print(EMPTY.repeat(1) + " ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private String pieceTranslator(ChessPiece piece){
        if (piece == null){
            return "";
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_PAWN;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_PAWN;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_BISHOP;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_BISHOP;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_KNIGHT;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_KNIGHT;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_KING;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_KING;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_ROOK;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_ROOK;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return WHITE_QUEEN;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return BLACK_QUEEN;
        }
        return "";
    }
}
