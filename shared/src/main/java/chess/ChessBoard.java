package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {



    ChessPiece[][] boardMatrix = new ChessPiece[8][8];

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

    public ChessPiece[][] getBoardMatrix() {
        return boardMatrix;
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

    public Collection<ChessPosition> getTeamPiecePositions(ChessGame.TeamColor teamColor) {
        Set<ChessPosition> positions = new HashSet<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                var position = new ChessPosition(row, col);
                ChessPiece piece = getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    positions.add(position);
                }
            }
        }

        return positions;
    }

    /**
     *
     * @param teamColor Color of king you want
     * @return position of king, or null if not found
     */
    public ChessPosition getTeamKingPosition(ChessGame.TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                var position = new ChessPosition(row, col);
                ChessPiece piece = getPiece(position);
                if (piece != null &&
                        piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(row, col);
                }
            }
        }

        return null;
    }

    public void movePiece(ChessMove move) {
        ChessPiece pieceToMove = getPiece(move.startPosition());

        if (pieceToMove == null) {
            return;
        }

        if (move.promotionPiece() != null) {
            pieceToMove = new ChessPiece(pieceToMove.getTeamColor(), move.promotionPiece());
        }

        addPiece(move.startPosition(), null);
        addPiece(move.endPosition(), pieceToMove);
        pieceToMove.setMoved();
    }

    public void handleEnPassant(ChessMove move) {
        var capturedPosition = new ChessPosition(move.startPosition().row(), move.endPosition().col());
        ChessPiece pieceToMove = getPiece(move.startPosition());

        addPiece(move.startPosition(), null);
        addPiece(move.endPosition(), pieceToMove);
        addPiece(capturedPosition, null);
        pieceToMove.setMoved();
    }

    public void handleCastle(ChessMove move) {
        boolean isQueenSide = move.startPosition().col() > move.endPosition().col();
        ChessPosition rookPosition = isQueenSide ? new ChessPosition(move.startPosition().row(), 1) :
                new ChessPosition(move.startPosition().row(), 8);
        ChessPiece rook = getPiece(rookPosition);
        ChessPiece king = getPiece(move.startPosition());

        if (isQueenSide) {
            addPiece(new ChessPosition(rookPosition.row(), 3), king);
            addPiece(move.startPosition(), null);
            addPiece(new ChessPosition(rookPosition.row(), 4), rook);
            addPiece(rookPosition, null);
        } else {
            addPiece(new ChessPosition(rookPosition.row(), 7), king);
            addPiece(move.startPosition(), null);
            addPiece(new ChessPosition(rookPosition.row(), 6), rook);
            addPiece(rookPosition, null);
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardMatrix, that.boardMatrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardMatrix);
    }


    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            ChessPiece[][] cloneBoardMatrix = new ChessPiece[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece piece = boardMatrix[i][j];
                    if (piece != null) {
                        cloneBoardMatrix[i][j] = piece.clone();
                    }
                }
            }
            clone.boardMatrix = cloneBoardMatrix;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
