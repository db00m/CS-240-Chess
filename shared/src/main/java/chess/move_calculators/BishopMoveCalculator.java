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

                if (pieceShouldNotAdvance(moves, newPosition)) break;
            }

            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + (i * j * -1), position.getColumn() + (i * j));

                if (pieceShouldNotAdvance(moves, newPosition)) break;
            }
        }

        return moves;
    }

    private boolean pieceShouldNotAdvance(Collection<ChessMove> moves, ChessPosition newPosition) {
        if (newPosition.isOffBoard()) {
            return true;
        }

        ChessPiece piece = board.getPiece(newPosition);

        if (piece == null) {
            moves.add(new ChessMove(position, newPosition));
            return false;
        } else if (piece.getTeamColor() != teamColor) {
            moves.add(new ChessMove(position, newPosition));
            return true;
        }

        return true;
    }
}
