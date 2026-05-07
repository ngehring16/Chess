package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;

    public ChessGame() {

    }
//    public boolean check_checker(int x, int y, ChessBoard board, ChessPiece check_piece){
//        if(x > 8 || x < 1 || y > 8 || y < 1){
//            return false;
//        }
//        ChessPosition checker_spot = new ChessPosition(y,x);
//        ChessPiece attacker = board.getPiece(checker_spot);
//
//        if (attacker != null && attacker.equals(check_piece)){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece KING = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for(int i = 1; i < 8; i++){
            for (int j = 1; j < 8; j++){
                ChessPosition spot = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(spot);
                if (piece != null) {
                    Collection<ChessMove> attacks = piece.pieceMoves(board, spot);
                    for (ChessMove moves : attacks) {
                        ChessPosition kill_zone = moves.getEndPosition();
                        ChessPiece dead = board.getPiece(kill_zone);
                        if (dead != null && dead.equals(KING)) {
                            return true;
                        }
                    }
                }
            }
        }
//        TeamColor opposite = null;
//        int col = 0;
//        int row = 0;
//        if (teamColor == TeamColor.WHITE) {
//            opposite = TeamColor.BLACK;
//            for (int i = 1; i < 8; i++) {
//                for (int j = 1; j < 8; j++) {
//                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
//                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
//                        col += j;
//                        row += i;
//                        i = 8;
//                        break;
//                    }
//                }
//            }
//        }
//        if (teamColor == TeamColor.BLACK) {
//            opposite = TeamColor.WHITE;
//            for (int i = 8; i > 0; i--) {
//                for (int j = 1; j < 8; j++) {
//                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
//                    if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
//                        col += j;
//                        row += i;
//                        i = 0;
//                        break;
//
//                    }
//                }
//            }
//        }
//        ChessPiece PAWN = new ChessPiece(opposite, ChessPiece.PieceType.PAWN);
//        ChessPiece KING = new ChessPiece(opposite, ChessPiece.PieceType.KING);
//        ChessPiece KNIGHT = new ChessPiece(opposite, ChessPiece.PieceType.KNIGHT);
//        ChessPiece ROOK  = new ChessPiece(opposite, ChessPiece.PieceType.ROOK);
//        ChessPiece BISHOP = new ChessPiece(opposite, ChessPiece.PieceType.BISHOP);
//        ChessPiece QUEEN = new ChessPiece(opposite, ChessPiece.PieceType.QUEEN);
//
//        //pawn check
//        if (check_checker(col-1, row-1, board, PAWN)){
//            return true;
//        }
//        if (check_checker(col+1, row-1, board, PAWN)){
//            return true;
//            }
//        //king check
//        if (check_checker(col-1, row-1, board, KING)){
//            return true;
//        }
//        if (check_checker(col+1, row-1, board, KING)){
//            return true;
//        }
//        if (check_checker(col+1, row, board, KING)){
//            return true;
//        }
//        if (check_checker(col-1, row, board, KING)){
//            return true;
//        }
//        if (check_checker(col, row-1, board, KING)){
//            return true;
//        }
//        if (check_checker(col, row+1, board, KING)){
//            return true;
//        }
//        if (check_checker(col-1, row+1, board, KING)){
//            return true;
//        }
//        if (check_checker(col+1, row+1, board, KING)){
//            return true;
//        }
//        //Rook/queen check
//
//        for (int i = row; i > 0; i--){
//            if (check_checker(col, i - 1, board, ROOK) || check_checker(col, i - 1, board, QUEEN)){
//                return true;
//            }
//        }
//        for (int i = col; i > 0; i--){
//            if (check_checker(i - 1, row, board, ROOK) || check_checker(i - 1, row, board, QUEEN)){
//                return true;
//            }
//        }
//        for (int i = row; i < 8; i++){
//            if (check_checker(col, i + 1, board, ROOK) || check_checker(col, i + 1, board, QUEEN)){
//                return true;
//            }
//        }
//        for (int i = col; i < 8; i++){
//            if (check_checker(i + 1, row, board, ROOK) || check_checker(i + 1, row, board, QUEEN)){
//                return true;
//            }
//        }
//
//
//        //Bishop/queen check
//        int i = col;
//        int j = row;
//        while (i < 8 && j < 8){
//            if (check_checker(i+1, j+1, board, BISHOP) || check_checker(i+1, j+1, board, QUEEN)){
//                return true;
//            }
//            i++;
//            j++;
//        }
//        i = col;
//        j = row;
//        while (j > 0 && i < 8){
//            if (check_checker(i+1, j-1, board, BISHOP) || check_checker(i+1, j-1, board, QUEEN)){
//                return true;
//            }
//            i++;
//            j--;
//        }
//        i = col;
//        j = row;
//        while (j < 8 && i > 0){
//            if (check_checker(i-1, j+1, board, BISHOP) || check_checker(i-1, j+1, board, QUEEN)){
//                return true;
//            }
//            i--;
//            j++;
//        }
//        i = col;
//        j = row;
//        while (i > 0 && j > 0){
//            if (check_checker(i-1, j-1, board, BISHOP) || check_checker(i-1, j-1, board, QUEEN)){
//                return true;
//            }
//            i--;
//            j--;
//        }
//
//
//        if (check_checker(col+1, row+2, board, KNIGHT)){
//            return true;
//        }
//        if (check_checker(col+2, row+1, board, KNIGHT)){
//            return true;
//        }
//        if (check_checker(col-1, row+2, board, KNIGHT)){
//            return true;
//        }
//        if(check_checker(col-2, row+1, board, KNIGHT)){
//            return true;
//        }
//        if(check_checker(col+2, row-1, board, KNIGHT)){
//            return true;
//        }
//        if(check_checker(col+1, row-2, board, KNIGHT)){
//            return true;
//        }
//        if (check_checker(col-1, row-2, board, KNIGHT)){
//            return true;
//        }
//        if(check_checker(col-2, row-1, board, KNIGHT)){
//            return true;
//        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }
}
