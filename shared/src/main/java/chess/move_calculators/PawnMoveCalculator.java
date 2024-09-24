package chess.move_calculators;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessBoard;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PawnMoveCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();
    int direction = 1;

    public PawnMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> calculate() {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        // Start Moves (can move forward 2)
        addStandardMoves();
        // Attack moves (can move diag 1 if attackable piece is in spot)
        addAttackMoves();
        // En Passant (not needed here... but maybe if I have time)
        return moves;
    };

    private void addStandardMoves() {

        for (int i = 1; i < 3; i++) {
            var newPosition = new ChessPosition(myPosition.row() + (i * direction), myPosition.col());

            if (newPosition.isOffBoard()) {
                break;
            }

            ChessPiece piece = board.getPiece(newPosition);

            if ((piece == null) && newPosition.row() == 8 || newPosition.row() == 1) {
                // promote piece
                addPromotionMoves(newPosition);
            } else if (piece == null) {
                moves.add(new ChessMove(myPosition, newPosition));
            }

           if (((myPosition.row() == 2) && (direction == 1)) || ((myPosition.row() == 7) && (direction == -1)) && piece == null) {
                continue;
            }

            break;
        }
    }

    private void addAttackMoves() {
        for (int i = -1; i < 2; i += 2) {
            var newPosition = new ChessPosition(myPosition.row() + direction, myPosition.col() + i);

            if (newPosition.isOffBoard()) {
                continue;
            }

            ChessPiece piece = board.getPiece(newPosition);

            if (piece != null && piece.getTeamColor() != teamColor) {
                if (newPosition.row() == 1 || newPosition.row() == 8) {
                    addPromotionMoves(newPosition);
                } else {
                    moves.add(new ChessMove(myPosition, newPosition));
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition newPosition) {
        ChessMove[] promotionMoves = {
                new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN),
                new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP),
                new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK),
                new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT)
        };

        moves.addAll(List.of(promotionMoves));
    }
}
