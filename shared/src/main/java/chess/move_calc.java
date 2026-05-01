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

    public Collection<ChessMove> Rook_move(ChessPiece Rook, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
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
        return List.of();
    }

    public Collection<ChessMove> Bishop_move(ChessPiece Bishop, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

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
        return List.of();
    }

    public Collection<ChessMove> Queen_move(ChessPiece Queen, ChessBoard board, ChessPosition position){
        return List.of();
    }

    public Collection<ChessMove> Pawn_move(ChessPiece Pawn, ChessBoard board, ChessPosition position){
        return List.of();
    }
}
