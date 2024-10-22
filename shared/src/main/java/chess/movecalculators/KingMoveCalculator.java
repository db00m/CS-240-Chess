package chess.movecalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KingMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();

    public KingMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> calculate() {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                var newPosition = new ChessPosition(myPosition.row() + i, myPosition.col() + j);

                KnightMoveCalculator.validateNewPosition(newPosition, board, teamColor, moves, myPosition);
            }
        }

        return moves;
    }
}
