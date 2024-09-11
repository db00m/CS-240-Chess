package chess;

import java.util.Collection;

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
        color = pieceColor;
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
        if (type == PieceType.KNIGHT && color == ChessGame.TeamColor.BLACK) {
            return "N";
        } else if (type == PieceType.KNIGHT) {
            return "n";
        }

        if (color == ChessGame.TeamColor.BLACK) {
            return type.toString().substring(0, 1).toUpperCase();
        } else {
            return type.toString().substring(0, 1).toLowerCase();
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

    private int startingColumn(int pieceIndex) {
        return switch (type) {
            case KING -> 4;
            case QUEEN -> 3;
            case BISHOP -> {
                if (pieceIndex == 0) {
                    yield 2;
                } else {
                    yield 5;
                }
            }
            case KNIGHT -> {
                if (pieceIndex == 0) {
                    yield 1;
                } else {
                    yield 6;
                }
            }
            case ROOK -> pieceIndex * 7; // 0 if index is 0 and 7 if index is 1
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
                return 6;
            } else {
                return 7;
            }
        } else {
            if (type == PieceType.PAWN) {
                return 1;
            } else {
                return 0;
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
        throw new RuntimeException("Not implemented");
    }
}
