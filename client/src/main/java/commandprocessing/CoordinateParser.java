package commandprocessing;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import client.InvalidParamsException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateParser {

    private static final Pattern COORD_PATTERN = Pattern.compile("^[a-h][1-8]$");

    ChessPosition parsePosition(String coord) throws InvalidParamsException {
        coord = coord.toLowerCase();

        validate(coord);
        int col = coord.charAt(0) - 'a' + 1;
        int row = Integer.parseInt(String.valueOf(coord.charAt(1)));
        return new ChessPosition(row, col);
    }

    ChessMove parseMove(String[] coords) throws InvalidParamsException {
        ChessPosition[] positions = new ChessPosition[2];

        for (int i = 0; i < 2; i++) {
            positions[i] = parsePosition(coords[i]);
        }

        try {
            if (coords.length > 2) {
                return new ChessMove(positions[0], positions[1], ChessPiece.PieceType.valueOf(coords[2].toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidParamsException("Provided promotion piece is not a valid piece");
        }

        return new ChessMove(positions[0], positions[1]);
    }

    private void validate(String coord) throws InvalidParamsException {
        Matcher matcher = COORD_PATTERN.matcher(coord);

        if (!matcher.matches()) {
            throw new InvalidParamsException("'" + coord + "'" + " is an invalid board coordinate");
        }
    }
}
