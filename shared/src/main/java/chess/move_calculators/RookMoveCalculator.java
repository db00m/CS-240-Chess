package chess.move_calculators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

/**
 * Rooks can move in the cardinal directions, stopped only by piece that get in it's way.
 */
public class RookMoveCalculator {

    ChessBoard board;
    ChessPosition position;
    ChessGame.TeamColor teamColor;

    public RookMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        Set<ChessMove> moves = new HashSet<>();

        for (int i = -1; i < 2; i += 2) {
            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + (j * i), position.getColumn()); // Up and Down

                ChessGame.MoveType moveType = calculateMoveType(newPosition);

                if (moveType == ChessGame.MoveType.ADVANCE) {
                    moves.add(new ChessMove(position, newPosition, null));
                } else if (moveType == ChessGame.MoveType.CAPTURE) {
                    moves.add(new ChessMove(position, newPosition, null));
                    break;
                } else if (moveType == ChessGame.MoveType.BLOCKED) {
                    break;
                }
            }

            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + (j * i)); // Side to side

                ChessGame.MoveType moveType = calculateMoveType(newPosition);

                if (moveType == ChessGame.MoveType.ADVANCE) {
                    moves.add(new ChessMove(position, newPosition, null));
                } else if (moveType == ChessGame.MoveType.CAPTURE) {
                    moves.add(new ChessMove(position, newPosition, null));
                    break;
                } else if (moveType == ChessGame.MoveType.BLOCKED) {
                    break;
                }
            }
        }

        return moves;
    }

    private ChessGame.MoveType calculateMoveType(ChessPosition newPosition) {
        if (newPosition.offBoard()) {
            return ChessGame.MoveType.BLOCKED;
        }

        ChessPiece piece = board.getPiece(newPosition);

        if (piece == null) {
            return ChessGame.MoveType.ADVANCE; // TODO: Overload ChessMove
        } else if (piece.getTeamColor() != teamColor) {
            return ChessGame.MoveType.CAPTURE;
        }

        return ChessGame.MoveType.BLOCKED;
    }
}
