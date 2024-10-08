package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public record ChessPosition(int row, int col) {
    public boolean isOffBoard() {
        return (row() > 8 || col() > 8) || (row() < 1 || col() < 1);
    }
}
