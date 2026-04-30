package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class move_calc {

    public move_calc(){

    }

    public Collection<ChessMove> Rook_move(ChessPiece Rook, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = null;
        var i = position.getRow();
        var j = position.getColumn();
        while (i < 8){
            ChessPosition next_position = new ChessPosition(i, j);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null) {
                if (next_piece.getTeamColor() == Rook.getTeamColor()) {
                    break;
                }
                if (next_piece.getTeamColor() != Rook.getTeamColor()) {
                    moves.add(current_move);
                    break;
                }
            }
            moves.add(current_move);
            i++;
        }
        while (j < 8){
            ChessPosition next_position = new ChessPosition(i, j);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null) {
                if (next_piece.getTeamColor() == Rook.getTeamColor()) {
                    break;
                }
                if (next_piece.getTeamColor() != Rook.getTeamColor()) {
                    moves.add(current_move);
                    break;
                }
            }
            moves.add(current_move);
            j++;
        }
        return moves;
    }

    public Collection<ChessMove> Knight_move(ChessPiece Knight, ChessBoard board, ChessPosition position){
        return List.of();
    }

    public Collection<ChessMove> Bishop_move(ChessPiece Bishop, ChessBoard board, ChessPosition position){
        return List.of();
    }

    public Collection<ChessMove> King_move(ChessPiece King, ChessBoard board, ChessPosition position){
        return List.of();
    }

    public Collection<ChessMove> Queen_move(ChessPiece Queen, ChessBoard board, ChessPosition position){
        return List.of();
    }

    public Collection<ChessMove> Pawn_move(ChessPiece Pawn, ChessBoard board, ChessPosition position){
        return List.of();
    }
}
