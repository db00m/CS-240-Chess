package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] boardArray = new ChessPiece[8][8];

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardArray[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Create new blank board
        boardArray = new ChessPiece[8][8];

        for (ChessGame.TeamColor color : ChessGame.TeamColor.values()) {
            // Add pawns to board
            for (int i = 1; i < 9; i++) {
                var pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                addPiece(pawn.getStartingPosition(i), pawn);
            }
            // Add 2 count pieces
            for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
                if (type == ChessPiece.PieceType.PAWN || type == ChessPiece.PieceType.KING || type == ChessPiece.PieceType.QUEEN) {
                    continue;
                } else {
                    for (int i = 0; i < 2; i++) {
                        var piece = new ChessPiece(color, type);
                        addPiece(piece.getStartingPosition(i), piece);
                    }
                }
            }
            // Add King and Queen
            var king = new ChessPiece(color, ChessPiece.PieceType.KING);
            addPiece(king.getStartingPosition(0), king);
            var queen = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
            addPiece(queen.getStartingPosition(0), queen);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardArray, that.boardArray);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardArray);
    }

    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (ChessPiece[] row : boardArray) {
            for (ChessPiece piece : row) {
                boardString.append("|");
                if (piece != null) {
                    boardString.append(piece);
                } else {
                    boardString.append(" ");
                }
            }
            boardString.append("|\n");
        }

        return boardString.toString();
    }
}
