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

                if (pieceShouldNotAdvance(moves, newPosition)) break;
            }

            for (int j = 1; j < 9; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + (j * i)); // Side to side

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
