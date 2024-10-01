package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public record ChessMove(MoveType moveType, ChessPosition startPosition, ChessPosition endPosition,
                       ChessPiece.PieceType promotionPiece) {

    public enum MoveType {
        BASIC,
        EN_PASSANT,
        CASTLE
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this(MoveType.BASIC, startPosition, endPosition, null);
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this(MoveType.BASIC, startPosition, endPosition, promotionPiece);
    }

    public ChessMove(MoveType moveType, ChessPosition startPosition, ChessPosition endPosition) {
        this(moveType, startPosition, endPosition, null);
    }

    public ChessPosition getStartPosition() { // Required for tests
        return startPosition;
    }

    public ChessPosition getEndPosition() {  // Required for tests
        return endPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(endPosition, chessMove.endPosition) && Objects.equals(startPosition, chessMove.startPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
