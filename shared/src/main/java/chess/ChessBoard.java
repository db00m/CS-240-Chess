package chess;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {



    ChessPiece[][] boardMatrix = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardMatrix[position.row()-1][position.col()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardMatrix[position.row()-1][position.col()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (ChessGame.TeamColor teamColor : ChessGame.TeamColor.values()) {

            // Add pawns
            for (int i = 0; i < 8; i++) {
                var pawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
                addPiece(pawn.startingPosition(i), pawn);
            }

            // Add 2 count pieces

            for (int i = 0; i < 2; i++) {
                ChessPiece[] doublePieces = {
                        new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP),
                        new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT),
                        new ChessPiece(teamColor, ChessPiece.PieceType.ROOK)
                };

                for (ChessPiece piece : doublePieces) {
                    addPiece(piece.startingPosition(i), piece);
                }
            }

            // add King and Queen

            ChessPiece[] royalPieces = {
                    new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN),
                    new ChessPiece(teamColor, ChessPiece.PieceType.KING)
            };

            for (ChessPiece piece : royalPieces) {
                addPiece(piece.startingPosition(0), piece);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ChessPiece[] row : boardMatrix) {
            for (ChessPiece piece : row) {
                stringBuilder.append("|");
                if (piece == null) {
                    stringBuilder.append(" ");
                } else {
                    stringBuilder.append(piece);
                }
            }
            stringBuilder.append("|\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardMatrix, that.boardMatrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardMatrix);
    }
}
