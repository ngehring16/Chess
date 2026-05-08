package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalc {

    public MoveCalc(){

    }

    public boolean mover(Collection<ChessMove> moves, ChessBoard board, int x, int y, ChessPosition position, ChessPiece piece){
        if(x > 8 || x < 1 || y > 8 || y < 1){
            return true;
        }
        ChessPosition nextSpot = new ChessPosition(y,x);
        ChessPiece nextPiece = board.getPiece(nextSpot);
        ChessMove nextMove = new ChessMove(position, nextSpot, null);
        if (nextPiece != null){
            if (nextPiece.getTeamColor() != piece.getTeamColor()){
                moves.add(nextMove);
                return true;
            }
            else{
                return true;
            }
        }
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            moves.add(nextMove);
        }
        return false;
    }

    public void promoter(ArrayList<ChessMove> moves, ChessBoard board, int x, int y, ChessPosition position, ChessPiece piece){
        if(x > 8 || x < 1 || y > 8 || y < 1){
            return;
        }
        ChessPosition nextSpot = new ChessPosition(y,x);
        ChessPiece nextPiece = board.getPiece(nextSpot);
        if (nextPiece != null){
            if (nextPiece.getTeamColor() != piece.getTeamColor()){
                moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.BISHOP));
                return;
            }
            else{
                return;
            }
        }
        if ((position.getRow() == y + 1 || position.getRow() == y -1) && position.getColumn() == x ) {
            moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(position, nextSpot, ChessPiece.PieceType.BISHOP));
        }
    }

    public Collection<ChessMove> rookMove(ChessPiece rook, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int y = position.getRow();
        int x = position.getColumn();
        for (int i = y; i > 0; i--){
            if (mover(moves, board, x, i - 1, position, rook)){
                break;
            }
        }
        for (int i = x; i > 0; i--){
            if(mover(moves, board, i -1, y, position, rook)){
                break;
            }
        }
        for (int i = y; i < 8; i++){
            if(mover(moves, board, x, i + 1, position, rook)){
                break;
            }
        }
        for (int i = x; i < 8; i++){
            if(mover(moves, board, i+1, y, position, rook )){
                break;
            }
        }
        return moves;
    }

    public Collection<ChessMove> knightMove(ChessPiece knight, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int x = position.getColumn();
        int y = position.getRow();

        mover(moves, board, x+1, y+2, position, knight);
        mover(moves, board, x+2, y+1, position, knight);
        mover(moves, board, x-1, y+2, position, knight);
        mover(moves, board, x-2, y+1, position, knight);
        mover(moves, board, x+2, y-1, position, knight);
        mover(moves, board, x+1, y-2, position, knight);
        mover(moves, board, x-1, y-2, position, knight);
        mover(moves, board, x-2, y-1, position, knight);
        return moves;
    }

    public Collection<ChessMove> bishopMove(ChessPiece bishop, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();

        int y = position.getRow();
        int x = position.getColumn();
        while (x < 8 && y < 8){
            if (mover(moves, board, x+1, y+1, position, bishop)){
                break;
            }
            x++;
            y++;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y > 0 && x < 8){
            if (mover(moves, board, x+1, y-1, position, bishop)){
                break;
            }
            x++;
            y--;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y < 8 && x > 0){
            if (mover(moves, board, x-1, y+1, position, bishop)){
                break;
            }
            x--;
            y++;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y > 0 && x > 0){
            if (mover(moves, board, x-1, y-1, position, bishop)){
                break;
            }
            x--;
            y--;
        }

        return moves;
    }

    public Collection<ChessMove> kingMove(ChessPiece king, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var y = position.getRow();
        var x = position.getColumn();

        mover(moves, board, x+1, y, position, king);
        mover(moves, board, x+1, y+1, position, king);
        mover(moves, board, x, y+1, position, king);
        mover(moves, board, x-1, y, position, king);
        mover(moves, board, x-1, y+1, position, king);
        mover(moves, board, x-1, y-1, position, king);
        mover(moves, board, x, y-1, position, king);
        mover(moves, board, x+1, y-1, position, king);
        return moves;
    }

    public Collection<ChessMove> queenMove(ChessPiece queen, ChessBoard board, ChessPosition position){
        Collection<ChessMove> diagonalMoves = bishopMove(queen, board, position);
        Collection<ChessMove> straightMoves = rookMove(queen, board, position);
        diagonalMoves.addAll(straightMoves);
        return diagonalMoves;
    }

    public Collection<ChessMove> pawnMove(ChessPiece pawn, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var y = position.getRow();
        var x = position.getColumn();

        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition nextSpot1 = new ChessPosition(y + 1, x);
            ChessPiece nextPiece1 = board.getPiece(nextSpot1);
            ChessMove currentMove = new ChessMove(position, nextSpot1, null);

            if (y == 7) {
               promoter(moves, board, x+1, y+1, position, pawn);
               promoter(moves, board, x-1, y+1, position, pawn);
               if (nextPiece1 == null) {
                   promoter(moves, board, x, y+1, position, pawn);
               }
               return moves;
           }
           mover(moves, board, x+1, y+1, position, pawn);
           mover(moves, board, x-1, y+1, position, pawn);
           if (nextPiece1 == null){
               moves.add(currentMove);
               if (y < 3) {
                   ChessPosition nextSpot2 = new ChessPosition(y + 2, x);
                   ChessPiece nextPiece2 = board.getPiece(nextSpot2);
                   ChessMove extraMove = new ChessMove(position, nextSpot2, null);
                   if (nextPiece2 == null) {
                       moves.add(extraMove);
                   }
               }
           }
        }

        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition nextSpot1 = new ChessPosition(y -1, x);
            ChessPiece nextPiece1 = board.getPiece(nextSpot1);
            ChessMove currentMove = new ChessMove(position, nextSpot1, null);

            if (y == 2) {
                promoter(moves, board, x-1, y-1, position, pawn);
                promoter(moves, board, x+1, y-1, position, pawn);
                if (nextPiece1 == null) {
                    promoter(moves, board, x, y-1, position, pawn);
                }
                return moves;
            }
            mover(moves, board, x-1, y-1, position, pawn);
            mover(moves, board, x+1, y-1, position, pawn);
            if (nextPiece1 == null){
                moves.add(currentMove);
                if (y > 6) {
                    ChessPosition nextSpot2 = new ChessPosition(y - 2, x);
                    ChessPiece nextPiece2 = board.getPiece(nextSpot2);
                    ChessMove extraMove = new ChessMove(position, nextSpot2, null);
                    if (nextPiece2 == null) {
                        moves.add(extraMove);
                    }
                }
            }
        }
       return moves;
    }
}
