package chess.move_calculators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class KnightMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor teamColor;

    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        Set<ChessMove> moves = new HashSet<>();

        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + (2 * i), position.getColumn() + j);

                if (moveIsValid(newPosition)) {
                    moves.add(new ChessMove(position, newPosition));
                }
            }

            for (int j = -1; j < 2; j += 2) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + j, position.getColumn() + (2 * i));

                if (moveIsValid(newPosition)) {
                    moves.add(new ChessMove(position, newPosition));
                }
            }
        }

        return moves;
    }

    /**
     * Determines whether the move to the newPosition is valid.  A move is invalid if it's off the board or if
     * A piece of the same color is on the space.
     * @param newPosition the position the piece will be moving to.
     * @return weather the move is valid
     */
    private boolean moveIsValid(ChessPosition newPosition) {
        if (newPosition.isOffBoard()) {
            return false;
        }

        ChessPiece piece = board.getPiece(newPosition);

        if (piece == null) {
            return true;
        }

        return piece.getTeamColor() != teamColor;
    }
}
