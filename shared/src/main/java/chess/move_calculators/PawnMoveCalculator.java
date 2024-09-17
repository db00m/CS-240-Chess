package chess.move_calculators;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

/**
 * Pawns have 3 move types
 * <ol>
 *     <li>Starting Move (forward 1 or 2)</li>
 *     <li>Basic Move (forward 1)</li>
 *     <li>Attack (diagonally forward)</li>
 *     <li>En Passant</li>
 * </ol>
 *
 * Many of these moves are straight forward, but En Passant can be a little complicated.  An En Passant Move can
 * only occur is the attacking pawn has moved exactly 3 ranks (squares).  If in this position, the enemy pawn on either
 * side has moved 2 ranks, the attacking pawn can move behind the enemy pawn, capturing it.
 */

public class PawnMoveCalculator {

    ChessBoard board;
    ChessPosition position;
    ChessGame.TeamColor teamColor;

    public PawnMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(basicMoves());
        moves.addAll(attackMoves());
        moves.addAll(enPassantMoves());

        return moves;
    }

    /**
     * Attack rules
     * @return set of all possible attack moves
     */
    private Collection<ChessMove> attackMoves() {
        HashSet<ChessMove> moves = new HashSet<>();
        for (int i = -1; i < 2; i += 2) {
            int row = position.getRow() + progressionDirection();
            int column = position.getColumn() + i;

            ChessPosition attackablePosition = new ChessPosition(row, column);
            ChessPiece attackablePiece = board.getPiece(attackablePosition);

            if (attackablePiece != null && attackablePiece.getTeamColor() != teamColor) {
                moves.add(new ChessMove(position, attackablePosition, null));
            }
        }

        return moves;
    }

    /**
     * This method includes both the starting move rule and the basic rule.
     * @return a set of all the possible basic moves
     */
    private Collection<ChessMove> basicMoves() {
        HashSet<ChessMove> moves = new HashSet<>();

        for (int i = 1; i < 3; i += 1) {
            if (i == 2 && notInStartPosition()) {
                break;
            }

            int row = position.getRow() + i * progressionDirection();

            if (row > 7 || row < 0) {
                break;
            }

            ChessPosition nextPosition = new ChessPosition(row, position.getColumn());

            if (board.getPiece(nextPosition) == null) {
                moves.add(new ChessMove(position, nextPosition, null));
            }
        }

        return moves;
    }

    private Collection<ChessMove> enPassantMoves() {
        HashSet<ChessMove> moves = new HashSet<>();

        if (pieceHasMoved3Ranks()) {
            for (int i = -1; i < 2; i += 1) {
                int column = position.getColumn() + i;
                ChessPosition enemyPosition = new ChessPosition(column, position.getRow());
                if (board.getPiece(enemyPosition) != null) {
                    var newPosition = new ChessPosition(enemyPosition.getColumn(), enemyPosition.getRow() + progressionDirection());
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }

        return moves;
    }

    /**
     *
     * @return whether the pawn as left its starting position
     */
    private boolean notInStartPosition() {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            return position.getRow() == 6;
        } else {
            return position.getColumn() == 1;
        }
    }

    /**
     * Calculates which direction the pawn should be moving based on its team color
     * @return 1 for white pieces and -1 for black
     */
    private int progressionDirection() {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            return -1;
        } else {
            return 1;
        }
    }

    private boolean pieceHasMoved3Ranks() {
        return (teamColor == ChessGame.TeamColor.BLACK && position.getRow() == 3) ||
                (teamColor == ChessGame.TeamColor.WHITE && position.getRow() == 4);
    }
}
