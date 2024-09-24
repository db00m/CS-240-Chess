package chess.move_calculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KnightMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();

    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> calculate() {

        for (int i = -2; i < 3; i += 4) {
            for (int j = -1; j < 2; j += 2) {
                var newPosition = new ChessPosition(myPosition.row() + i, myPosition.col() + j);

                validateNewPosition(newPosition);
            }
            for (int j = -1; j < 2; j += 2) {
                var newPosition = new ChessPosition(myPosition.row() + j, myPosition.col() + i);

                validateNewPosition(newPosition);
            }
        }

        return moves;
    }

    private void validateNewPosition(ChessPosition newPosition) {
        if (!newPosition.isOffBoard()) {
            ChessPiece piece = board.getPiece(newPosition);

            if (piece == null || piece.getTeamColor() != teamColor) {
                moves.add(new ChessMove(myPosition, newPosition));
            }
        }
    }
}
