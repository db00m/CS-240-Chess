package chess;

import chess.move_calculators.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    final static Map<PieceType, String> WHITE_PIECE_MAPPING = Map.of(
            PieceType.PAWN, "P",
            PieceType.KNIGHT, "N",
            PieceType.ROOK, "R",
            PieceType.QUEEN, "Q",
            PieceType.KING, "K",
            PieceType.BISHOP, "B");

    final static Map<PieceType, String> BLACK_PIECE_MAPPING = Map.of(
            PieceType.PAWN, "p",
            PieceType.KNIGHT, "n",
            PieceType.ROOK, "r",
            PieceType.QUEEN, "q",
            PieceType.KING, "k",
            PieceType.BISHOP, "b");

    private final ChessGame.TeamColor teamColor;
    private final PieceType type;
    private boolean moved = false;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.teamColor = pieceColor;
        this.type = type;
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved() {
        moved = true;
    }

    public ChessPosition startingPosition(int pieceIndex) {
        return new ChessPosition(startingRow(), startingCol(pieceIndex));
    }

    private int startingRow() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (type == PieceType.PAWN) {
                return 2;
            } else {
                return 1;
            }
        } else {
            if (type == PieceType.PAWN) {
                return 7;
            } else {
                return 8;
            }
        }
    }

    private int startingCol(int pieceIndex) {
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
            case PAWN -> pieceIndex + 1;
        };
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
            case KING -> new KingMoveCalculator(board, myPosition, teamColor).calculate();
            case QUEEN -> new QueenMoveCalculator(board, myPosition, teamColor).calculate();
            case BISHOP -> new BishopMoveCalculator(board, myPosition, teamColor).calculate();
            case KNIGHT -> new KnightMoveCalculator(board, myPosition, teamColor).calculate();
            case ROOK -> new RookMoveCalculator(board, myPosition, teamColor).calculate();
            case PAWN -> new PawnMoveCalculator(board, myPosition, teamColor).calculate();
        };
    }


    @Override
    public String toString() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            return WHITE_PIECE_MAPPING.get(type);
        } else {
            return BLACK_PIECE_MAPPING.get(type);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }
}
