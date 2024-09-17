package chess;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public String toString() {
        if (color == ChessGame.TeamColor.BLACK) {
            return switch (type) {
                case PieceType.KING -> "♔";
                case PieceType.QUEEN -> "♕";
                case PieceType.ROOK -> "♖";
                case PieceType.BISHOP -> "♗";
                case PieceType.KNIGHT -> "♘";
                case PieceType.PAWN -> "♙";
            };
        } else {
            return switch (type) {
                case PieceType.KING -> "♚";
                case PieceType.QUEEN -> "♛";
                case PieceType.ROOK -> "♜";
                case PieceType.BISHOP -> "♝";
                case PieceType.KNIGHT -> "♞";
                case PieceType.PAWN -> "♟";
            };
        }
    }

    public ChessPosition getStartingPosition(int pieceIndex) {
        return new ChessPosition(startingRow(), startingColumn(pieceIndex));
    }

    /**
     *
     * @param pieceIndex for pieces that have more than one, what is the current count.
     * @return starting column of the piece (for use in ChessPosition)
     */

    private int startingColumn(int pieceIndex) { // we might be able to store piece index as a static var
        return switch (type) {
            case KING -> 5;
            case QUEEN -> 4;
            case BISHOP -> {
                if (pieceIndex == 0) {
                    yield 3;
                } else {
                    yield 6;
                }
            }
            case KNIGHT -> {
                if (pieceIndex == 0) {
                    yield 2;
                } else {
                    yield 7;
                }
            }
            case ROOK -> {
                if (pieceIndex == 0) {
                    yield 1;
                } else {
                    yield 8;
                }
            }
            case PAWN -> pieceIndex;
        };
    }

    /**
     * Calculates the starting row of the piece.
     * @return starting row (for use in ChessPosition)
     */
    private int startingRow() {
        if (color == ChessGame.TeamColor.BLACK) {
            if (type == PieceType.PAWN) {
                return 7;
            } else {
                return 8;
            }
        } else {
            if (type == PieceType.PAWN) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case PAWN -> new chess.move_calculators.PawnMoveCalculator(board, myPosition, color).pieceMoves();
            case KING -> new chess.move_calculators.KingMoveCalculator(board, myPosition, color).pieceMoves();
            case ROOK -> new chess.move_calculators.RookMoveCalculator(board, myPosition, color).pieceMoves();
            case BISHOP -> new chess.move_calculators.BishopMoveCalculator(board, myPosition, color).pieceMoves();
            default -> Collections.emptySet();
        };
    }
}
