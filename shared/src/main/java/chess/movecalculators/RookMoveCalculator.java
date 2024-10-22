package chess.movecalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RookMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();

    public RookMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> calculate() {

        for (int i = -1; i < 2; i += 2) {
            for (int j = 1; j < 8; j++) {
                var newPosition = new ChessPosition(myPosition.row() + (i*j), myPosition.col());  // row +-, col 0

                if (!BishopMoveCalculator.pieceShouldAdvance(newPosition, board, moves, myPosition, teamColor)) { break; }
            }
            for (int j = 1; j < 8; j++) {
                var newPosition = new ChessPosition(myPosition.row(), myPosition.col() + (i*j));  // row +-, col 0

                if (!BishopMoveCalculator.pieceShouldAdvance(newPosition, board, moves, myPosition, teamColor)) { break; }
            }
        }

        return moves;
    }
}
