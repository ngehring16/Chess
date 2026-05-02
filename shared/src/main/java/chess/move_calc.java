package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class move_calc {

    public move_calc(){

    }

    public int mover(Collection<ChessMove> moves, ChessPiece piece, ChessPiece next_piece, ChessMove current_move){
        if (next_piece.getTeamColor() == piece.getTeamColor()) {
            return 1;
        }
        if (next_piece.getTeamColor() != piece.getTeamColor()) {
            moves.add(current_move);
            return 0;
        }
        return 2;
    }

    public void one_by_one(Collection<ChessMove> moves, ChessBoard board, int x, int y, ChessPosition position, ChessPiece piece){
        if(x > 8 || x < 1 || y > 8 || y < 1){
            return;
        }
        ChessPosition next_spot = new ChessPosition(y,x);
        ChessPiece next_piece = board.getPiece(next_spot);
        ChessMove next_move = new ChessMove(position, next_spot, null);
        if (next_piece != null){
            if (next_piece.getTeamColor() != piece.getTeamColor()){
                moves.add(next_move);
                return;
            }
            else{
                return;
            }
        }
        moves.add(next_move);
    }

    public void promote(ArrayList<ChessMove> moves, ChessPosition position1, ChessPosition position2){
        moves.add(new ChessMove(position1, position2, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(position1, position2, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position1, position2, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(position1, position2, ChessPiece.PieceType.BISHOP));
    }

    public Collection<ChessMove> Rook_move(ChessPiece Rook, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var x = position.getRow();
        var y = position.getColumn();
        while (x > 1){
            ChessPosition next_position = new ChessPosition(x - 1, y);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
               if (mover(moves, Rook, next_piece, current_move) < 2){
                   break;
               }
            }
            moves.add(current_move);
            x--;
        }

        var a = position.getRow();
        var b = position.getColumn();
        while (b > 1){
            ChessPosition next_position = new ChessPosition(a, b - 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Rook, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            b--;
        }

        var i = position.getRow();
        var j = position.getColumn();
        while (i < 8){
            ChessPosition next_position = new ChessPosition(i + 1, j);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Rook, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            i++;
        }
        var r = position.getRow();
        var c = position.getColumn();
        while (c < 8){
            ChessPosition next_position = new ChessPosition(r, c + 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Rook, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            c++;
        }
        return moves;
    }

    public Collection<ChessMove> Knight_move(ChessPiece Knight, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int x = position.getColumn();
        int y = position.getRow();

        one_by_one(moves, board, x+1, y+2, position, Knight);
        one_by_one(moves, board, x+2, y+1, position, Knight);
        one_by_one(moves, board, x-1, y+2, position, Knight);
        one_by_one(moves, board, x-2, y+1, position, Knight);
        one_by_one(moves, board, x+2, y-1, position, Knight);
        one_by_one(moves, board, x+1, y-2, position, Knight);
        one_by_one(moves, board, x-1, y-2, position, Knight);
        one_by_one(moves, board, x-2, y-1, position, Knight);
        return moves;
    }

    public Collection<ChessMove> Bishop_move(ChessPiece Bishop, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();

        var r = position.getRow();
        var c = position.getColumn();
        while (c < 8 && r < 8){
            ChessPosition next_position = new ChessPosition(r + 1, c + 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Bishop, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            c++;
            r++;
        }

        var i = position.getRow();
        var j = position.getColumn();
        while (i > 1 && j < 8){
            ChessPosition next_position = new ChessPosition(i - 1, j + 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Bishop, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            i--;
            j++;
        }

        var a = position.getRow();
        var b = position.getColumn();
        while (a < 8 && b > 1){
            ChessPosition next_position = new ChessPosition(a + 1, b - 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Bishop, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            a++;
            b--;
        }

        var x = position.getRow();
        var y = position.getColumn();
        while (x > 1 && y > 1){
            ChessPosition next_position = new ChessPosition(x - 1, y - 1);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, Bishop, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            x--;
            y--;
        }

        return moves;
    }

    public Collection<ChessMove> King_move(ChessPiece King, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        var y = position.getRow();
        var x = position.getColumn();
        while (x > 1){
            ChessPosition next_position = new ChessPosition(x - 1, y);
            ChessPiece next_piece = board.getPiece(next_position);
            ChessMove current_move = new ChessMove(position, next_position, null);
            if (next_piece != null){
                if (mover(moves, King, next_piece, current_move) < 2){
                    break;
                }
            }
            moves.add(current_move);
            x--;
        }
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
        if (Pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (y < 8) {
                if (x < 8) {
                    ChessPosition attack_position1 = new ChessPosition(y + 1, x + 1);
                    ChessPiece attack_piece1 = board.getPiece(attack_position1);
                    ChessMove attack_move1 = new ChessMove(position, attack_position1, null);
                    if (y == 7 && attack_piece1 != null && attack_piece1.getTeamColor() != Pawn.getTeamColor()) {
                        promote(moves, position, attack_position1);
                    }
                    else if (attack_piece1 != null && attack_piece1.getTeamColor() != Pawn.getTeamColor()) {
                        moves.add(attack_move1);
                    }
                }
                if (x > 1) {
                    ChessPosition attack_position2 = new ChessPosition(y + 1, x - 1);
                    ChessPiece attack_piece2 = board.getPiece(attack_position2);
                    ChessMove attack_move2 = new ChessMove(position, attack_position2, null);
                    if (y == 7 && attack_piece2 != null && attack_piece2.getTeamColor() != Pawn.getTeamColor()) {
                        promote(moves, position, attack_position2);
                    }
                    else if (attack_piece2 != null && attack_piece2.getTeamColor() != Pawn.getTeamColor()) {
                        moves.add(attack_move2);
                    }
                }
                ChessPosition next_spot1 = new ChessPosition(y + 1, x);
                ChessPiece next_piece1 = board.getPiece(next_spot1);
                ChessMove current_move = new ChessMove(position, next_spot1, null);
                if (y == 7 && next_piece1 == null){
                    promote(moves, position, next_spot1);
                }
                else if (next_piece1 == null) {
                    moves.add(current_move);
                }

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
            if (y > 1) {
                if (x > 1) {
                    ChessPosition attack_position1 = new ChessPosition(y - 1, x - 1);
                    ChessPiece attack_piece1 = board.getPiece(attack_position1);
                    ChessMove attack_move1 = new ChessMove(position, attack_position1, null);
                    if (y == 2 && attack_piece1 != null && attack_piece1.getTeamColor() != Pawn.getTeamColor()) {
                        promote(moves, position, attack_position1);
                    }
                    else if (attack_piece1 != null && attack_piece1.getTeamColor() != Pawn.getTeamColor()) {
                        moves.add(attack_move1);
                    }
                }
                if (x < 8) {
                    ChessPosition attack_position2 = new ChessPosition(y - 1, x + 1);
                    ChessPiece attack_piece2 = board.getPiece(attack_position2);
                    ChessMove attack_move2 = new ChessMove(position, attack_position2, null);
                    if (y == 2 && attack_piece2 != null && attack_piece2.getTeamColor() != Pawn.getTeamColor()) {
                        promote(moves, position, attack_position2);
                    }
                    else if (attack_piece2 != null && attack_piece2.getTeamColor() != Pawn.getTeamColor()) {
                        moves.add(attack_move2);
                    }
                }
                ChessPosition next_spot1 = new ChessPosition(y - 1, x);
                ChessPiece next_piece1 = board.getPiece(next_spot1);
                ChessMove current_move = new ChessMove(position, next_spot1, null);
                if (y == 2 && next_piece1 == null){
                    promote(moves, position, next_spot1);
                }
                else if (next_piece1 == null) {
                    moves.add(current_move);
                }

                if (y > 6) {
                    ChessPosition next_spot2 = new ChessPosition(y - 2, x);
                    ChessPiece next_piece2 = board.getPiece(next_spot2);
                    ChessMove extra_move = new ChessMove(position, next_spot2, null);
                    if (next_piece2 == null && next_piece1 == null) {
                        moves.add(extra_move);
                    }
                }
            }
        }
        return moves;
    }
}
