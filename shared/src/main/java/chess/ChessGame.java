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
    private ChessBoard board = new ChessBoard();
    private TeamColor team;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        board.resetBoard();
    }

    public boolean hasValidMoves(TeamColor teamColor){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j =1; j<9; j++){
                ChessPosition spot = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(spot);
                if (piece != null && piece.getTeamColor().equals(teamColor)){
                    validMoves.addAll(validMoves(spot));
                }
            }
        }
        return validMoves.isEmpty();
    }

    public void movePiece(ChessPosition start, ChessPosition end, ChessBoard board, ChessPiece.PieceType promotion){

        ChessPiece piece = board.getPiece(start);
        if(promotion != null){
            piece = new ChessPiece(piece.getTeamColor(), promotion);
        }
        board.addPiece(end, piece);
        board.addPiece(start, null);
    }

    public boolean checkChecker(TeamColor teamColor, ChessBoard board){
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> attacks = new ArrayList<>();
        for(int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition spot = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(spot);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    attacks = piece.pieceMoves(board, spot);
                }
                for (ChessMove moves : attacks) {
                    ChessPosition killZone = moves.getEndPosition();
                    ChessPiece dead = board.getPiece(killZone);
                    if (dead != null && dead.equals(king)) {
                        return true;
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
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        try{
            ChessPiece piece = board.getPiece(startPosition);
            TeamColor color = piece.getTeamColor();
            Collection<ChessMove> attacks = piece.pieceMoves(board, startPosition);
            for (ChessMove move : attacks){
                ChessBoard clonedBoard = (ChessBoard)board.clone();
                movePiece(move.getStartPosition(), move.getEndPosition(), clonedBoard, move.getPromotionPiece());
                if (!checkChecker(color, clonedBoard)){
                   validMoves.add(move);
                }
            }
        }
        catch (CloneNotSupportedException E){System.out.println("Got a clone not supported exception");}
        return validMoves;
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
        ChessPiece.PieceType promotion = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        TeamColor enemy = TeamColor.BLACK;
        if (piece == null){
            throw new InvalidMoveException();
        }
        if (piece.getTeamColor() == TeamColor.BLACK){
            enemy = TeamColor.WHITE;
        }
        if (getTeamTurn() != piece.getTeamColor()){
            throw new InvalidMoveException();
        }
        for (ChessMove moves : validMoves(start)){
            if (moves.equals(move)){
                movePiece(start, end, board, promotion);
                setTeamTurn(enemy);
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
        return checkChecker(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && hasValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && hasValidMoves(teamColor);
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
