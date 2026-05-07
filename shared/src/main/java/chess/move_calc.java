package chess;

import java.util.ArrayList;
import java.util.Collection;

public class move_calc {

    public move_calc(){

    }

    public boolean mover(Collection<ChessMove> moves, ChessBoard board, int x, int y, ChessPosition position, ChessPiece piece){
        if(x > 8 || x < 1 || y > 8 || y < 1){
            return true;
        }
        ChessPosition next_spot = new ChessPosition(y,x);
        ChessPiece next_piece = board.getPiece(next_spot);
        ChessMove next_move = new ChessMove(position, next_spot, null);
        if (next_piece != null){
            if (next_piece.getTeamColor() != piece.getTeamColor()){
                moves.add(next_move);
                return true;
            }
            else{
                return true;
            }
        }
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            moves.add(next_move);
        }
        return false;
    }

    public void promoter(ArrayList<ChessMove> moves, ChessBoard board, int x, int y, ChessPosition position, ChessPiece piece){
        if(x > 8 || x < 1 || y > 8 || y < 1){
            return;
        }
        ChessPosition next_spot = new ChessPosition(y,x);
        ChessPiece next_piece = board.getPiece(next_spot);
        if (next_piece != null){
            if (next_piece.getTeamColor() != piece.getTeamColor()){
                moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.BISHOP));
                return;
            }
            else{
                return;
            }
        }
        if ((position.getRow() == y + 1 || position.getRow() == y -1) && position.getColumn() == x ) {
            moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(position, next_spot, ChessPiece.PieceType.BISHOP));
        }
    }

    public Collection<ChessMove> Rook_move(ChessPiece Rook, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int y = position.getRow();
        int x = position.getColumn();
        for (int i = y; i > 0; i--){
            if (mover(moves, board, x, i - 1, position, Rook)){
                break;
            }
        }
        for (int i = x; i > 0; i--){
            if(mover(moves, board, i -1, y, position, Rook)){
                break;
            }
        }
        for (int i = y; i < 8; i++){
            if(mover(moves, board, x, i + 1, position, Rook)){
                break;
            }
        }
        for (int i = x; i < 8; i++){
            if(mover(moves, board, i+1, y, position, Rook )){
                break;
            }
        }
        return moves;
    }

    public Collection<ChessMove> Knight_move(ChessPiece Knight, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int x = position.getColumn();
        int y = position.getRow();

        mover(moves, board, x+1, y+2, position, Knight);
        mover(moves, board, x+2, y+1, position, Knight);
        mover(moves, board, x-1, y+2, position, Knight);
        mover(moves, board, x-2, y+1, position, Knight);
        mover(moves, board, x+2, y-1, position, Knight);
        mover(moves, board, x+1, y-2, position, Knight);
        mover(moves, board, x-1, y-2, position, Knight);
        mover(moves, board, x-2, y-1, position, Knight);
        return moves;
    }

    public Collection<ChessMove> Bishop_move(ChessPiece Bishop, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();

        int y = position.getRow();
        int x = position.getColumn();
        while (x < 8 && y < 8){
            if (mover(moves, board, x+1, y+1, position, Bishop)){
                break;
            }
            x++;
            y++;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y > 0 && x < 8){
            if (mover(moves, board, x+1, y-1, position, Bishop)){
                break;
            }
            x++;
            y--;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y < 8 && x > 0){
            if (mover(moves, board, x-1, y+1, position, Bishop)){
                break;
            }
            x--;
            y++;
        }

        y = position.getRow();
        x = position.getColumn();
        while (y > 0 && x > 0){
            if (mover(moves, board, x-1, y-1, position, Bishop)){
                break;
            }
            x--;
            y--;
        }

        return moves;
    }

    public Collection<ChessMove> King_move(ChessPiece King, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var y = position.getRow();
        var x = position.getColumn();

        mover(moves, board, x+1, y, position, King);
        mover(moves, board, x+1, y+1, position, King);
        mover(moves, board, x, y+1, position, King);
        mover(moves, board, x-1, y, position, King);
        mover(moves, board, x-1, y+1, position, King);
        mover(moves, board, x-1, y-1, position, King);
        mover(moves, board, x, y-1, position, King);
        mover(moves, board, x+1, y-1, position, King);
        return moves;
    }

    public Collection<ChessMove> Queen_move(ChessPiece Queen, ChessBoard board, ChessPosition position){
        Collection<ChessMove> diagonal_moves = Bishop_move(Queen, board, position);
        Collection<ChessMove> straight_moves = Rook_move(Queen, board, position);
        diagonal_moves.addAll(straight_moves);
        return diagonal_moves;
    }

    public Collection<ChessMove> Pawn_move(ChessPiece Pawn, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var y = position.getRow();
        var x = position.getColumn();

        if (Pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition next_spot1 = new ChessPosition(y + 1, x);
            ChessPiece next_piece1 = board.getPiece(next_spot1);
            ChessMove current_move = new ChessMove(position, next_spot1, null);

            if (y == 7) {
               promoter(moves, board, x+1, y+1, position, Pawn);
               promoter(moves, board, x-1, y+1, position, Pawn);
               if (next_piece1 == null) {
                   promoter(moves, board, x, y+1, position, Pawn);
               }
               return moves;
           }
           mover(moves, board, x+1, y+1, position, Pawn);
           mover(moves, board, x-1, y+1, position, Pawn);
           if (next_piece1 == null){
               moves.add(current_move);
               if (y < 3) {
                   ChessPosition next_spot2 = new ChessPosition(y + 2, x);
                   ChessPiece next_piece2 = board.getPiece(next_spot2);
                   ChessMove extra_move = new ChessMove(position, next_spot2, null);
                   if (next_piece2 == null) {
                       moves.add(extra_move);
                   }
               }
           }
        }

        if (Pawn.getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition next_spot1 = new ChessPosition(y -1, x);
            ChessPiece next_piece1 = board.getPiece(next_spot1);
            ChessMove current_move = new ChessMove(position, next_spot1, null);

            if (y == 2) {
                promoter(moves, board, x-1, y-1, position, Pawn);
                promoter(moves, board, x+1, y-1, position, Pawn);
                if (next_piece1 == null) {
                    promoter(moves, board, x, y-1, position, Pawn);
                }
                return moves;
            }
            mover(moves, board, x-1, y-1, position, Pawn);
            mover(moves, board, x+1, y-1, position, Pawn);
            if (next_piece1 == null){
                moves.add(current_move);
                if (y > 6) {
                    ChessPosition next_spot2 = new ChessPosition(y - 2, x);
                    ChessPiece next_piece2 = board.getPiece(next_spot2);
                    ChessMove extra_move = new ChessMove(position, next_spot2, null);
                    if (next_piece2 == null) {
                        moves.add(extra_move);
                    }
                }
            }
        }
       return moves;
    }
}
