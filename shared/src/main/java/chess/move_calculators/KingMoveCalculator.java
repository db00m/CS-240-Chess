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
 * The King can move one space out in all directions unless it is blocked by pieces from its own team.
 */
public class KingMoveCalculator {

    ChessBoard board;
    ChessPosition position;
    ChessGame.TeamColor teamColor;

    public KingMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        Set<ChessMove> moves = new HashSet<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + i, position.getColumn() + j);

                if (newPosition.isOffBoard()) {  // new position is outside board
                    continue;
                }

                ChessPiece piece = board.getPiece(newPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {  // blocked by piece
                    continue;
                }

                moves.add(new ChessMove(position, newPosition));
            }
        }

        return moves;
    }


}
