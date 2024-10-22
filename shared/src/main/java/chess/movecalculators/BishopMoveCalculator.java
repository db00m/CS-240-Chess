package chess.movecalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BishopMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();

    public BishopMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }


    public Collection<ChessMove> calculate() {
        for (int i = -1; i < 2; i += 2) {
            for (int j = 1; j < 8; j++) {
                var newPosition = new ChessPosition(myPosition.row() + (i*j), myPosition.col() + (i*j));  // row +, col 0 +

                if (!pieceShouldAdvance(newPosition, board, moves, myPosition, teamColor)) { break; }
            }
            for (int j = 1; j < 8; j++) {
                var newPosition = new ChessPosition(myPosition.row() - (i*j), myPosition.col() + (i*j));  // row +, col 0 -

                if (!pieceShouldAdvance(newPosition, board, moves, myPosition, teamColor)) { break; }
            }
        }

        return moves;
    }

    static boolean pieceShouldAdvance(ChessPosition newPosition, ChessBoard board, Set<ChessMove> moves, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        if (newPosition.isOffBoard()) {
            return false;
        }

        ChessPiece piece = board.getPiece(newPosition);

        if (piece == null) {
            moves.add(new ChessMove(myPosition, newPosition));  // space free
            return true;
        } else if (piece.getTeamColor() != teamColor) {
            moves.add(new ChessMove(myPosition, newPosition));  // space occupied by enemy
            return false;
        }

        return false;
    }
}
