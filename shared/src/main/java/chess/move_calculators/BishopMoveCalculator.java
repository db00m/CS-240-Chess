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
 * Bishop moves Diagonally in all directions unless stopped by piece
 */
public class BishopMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor teamColor;

    public BishopMoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        Set<ChessMove> moves = new HashSet<>();

        for (int i = -1; i < 2; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + (i * j), position.getColumn() + (i * j));

                if (IsValidMove(moves, newPosition)) break;
            }

            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + (i * j * -1), position.getColumn() + (i * j));

                if (IsValidMove(moves, newPosition)) break;
            }
        }

        return moves;
    }

    private boolean IsValidMove(Set<ChessMove> moves, ChessPosition newPosition) {
        ChessGame.MoveType moveType = calculateMoveType(newPosition);

        if (moveType == ChessGame.MoveType.BLOCKED) {
            return true;
        } else if (moveType == ChessGame.MoveType.CAPTURE) {
            moves.add(new ChessMove(position, newPosition));
            return true;
        }

        moves.add(new ChessMove(position, newPosition));
        return false;
    }

    private ChessGame.MoveType calculateMoveType(ChessPosition newPosition) {
        if (newPosition.isOffBoard()) {
            return ChessGame.MoveType.BLOCKED;
        }

        ChessPiece piece = board.getPiece(newPosition);

        if (piece == null) {
            return ChessGame.MoveType.ADVANCE;
        } else if (piece.getTeamColor() != teamColor) {
            return ChessGame.MoveType.CAPTURE;
        }

        return ChessGame.MoveType.BLOCKED;
    }
}
