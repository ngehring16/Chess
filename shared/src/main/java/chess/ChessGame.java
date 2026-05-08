package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor team;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        board.resetBoard();
    }

    public boolean has_valid_moves(TeamColor teamColor){
        Collection<ChessMove> valid_moves = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j =1; j<9; j++){
                ChessPosition spot = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(spot);
                if (piece != null && piece.getTeamColor().equals(teamColor)){
                    valid_moves.addAll(validMoves(spot));
                }
            }
        }
        return valid_moves.isEmpty();
    }

    public void move_piece(ChessPosition start, ChessPosition end, ChessBoard board){
        ChessPiece piece = board.getPiece(start);
        board.addPiece(end, piece);
        board.addPiece(start, null);
    }

    public boolean check_checker(TeamColor teamColor, ChessBoard board){
        ChessPiece KING = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for(int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition spot = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(spot);
                if (piece != null && piece.getTeamColor() != teamColor) {
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
        return false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
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
        ArrayList<ChessMove> valid_moves = new ArrayList<>();
        try{
            ChessPiece piece = board.getPiece(startPosition);
            TeamColor color = piece.getTeamColor();
            Collection<ChessMove> attacks = piece.pieceMoves(board, startPosition);
            for (ChessMove move : attacks){
                ChessBoard cloned_board = (ChessBoard)board.clone();
                move_piece(move.getStartPosition(), move.getEndPosition(), cloned_board);
                if (!check_checker(color, cloned_board)){
                   valid_moves.add(move);
                }
            }
        }
        catch (CloneNotSupportedException E){System.out.println("Got a clone not supported exception");}
        return valid_moves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        for (ChessMove moves : validMoves(start)){
            if (moves.equals(move)){
                move_piece(start, end, board);
                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return check_checker(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && has_valid_moves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && has_valid_moves(teamColor);
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
        return Objects.equals(board, chessGame.board) && team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, team);
    }
}
