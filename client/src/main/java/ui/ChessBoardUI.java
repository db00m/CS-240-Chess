package ui;

import chess.*;
import client.StateManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    private static final String BLACK_BOARDER_SQUARE = square(SET_BG_COLOR_LIGHT_GREY, EMPTY);
    private static final String[] COLUMNS = { " A ", " B ", " C ", " D ", " E ", " F ", " G ", " H " };
    private static final String[] ROWS = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };

    private final static Map<ChessPiece.PieceType, String> WHITE_PIECE_MAPPING = Map.of(
            ChessPiece.PieceType.PAWN, WHITE_PAWN,
            ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT,
            ChessPiece.PieceType.ROOK, WHITE_ROOK,
            ChessPiece.PieceType.QUEEN, WHITE_QUEEN,
            ChessPiece.PieceType.KING, WHITE_KING,
            ChessPiece.PieceType.BISHOP, WHITE_BISHOP);
    private final static Map<ChessPiece.PieceType, String> BLACK_PIECE_MAPPING = Map.of(
            ChessPiece.PieceType.PAWN, BLACK_PAWN,
            ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT,
            ChessPiece.PieceType.ROOK, BLACK_ROOK,
            ChessPiece.PieceType.QUEEN, BLACK_QUEEN,
            ChessPiece.PieceType.KING, BLACK_KING,
            ChessPiece.PieceType.BISHOP, BLACK_BISHOP);

    private final StateManager stateManager;

    private int iterationStart;
    private int iterationEnd;
    private int iterationDirection;

    private final Collection<ChessPosition> highlightedSpaces = new HashSet<>();
    private ChessPosition focusPosition = null;

    public ChessBoardUI(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setIterationVars() {
        if (stateManager.getTeamColor() == ChessGame.TeamColor.BLACK) {
            iterationStart = 7;
            iterationEnd = -1;
            iterationDirection = -1;
        } else {
            iterationStart = 0;
            iterationEnd = 8;
            iterationDirection = 1;
        }
    }

    public String withHighlighting(ChessPosition piecePosition) {
        focusPosition = piecePosition;
        for (ChessMove move : stateManager.getGameState().validMoves(piecePosition)) {
            highlightedSpaces.add(move.endPosition());
        }

        String boardString = toString();
        highlightedSpaces.clear();
        focusPosition = null;

        return boardString;
    }

    @Override
    public String toString() {
        setIterationVars();
        return header() + gameBoard() + header();
    }

    private String header() {
        StringBuilder headerString = new StringBuilder();
        headerString.append(SET_TEXT_BOLD);
        headerString.append(BLACK_BOARDER_SQUARE);
        for (int i = iterationStart; i < iterationEnd || i > iterationEnd; i += iterationDirection) {
            headerString.append(square(SET_BG_COLOR_LIGHT_GREY, occupantString(SET_TEXT_COLOR_BLACK, COLUMNS[i])));
        }
        headerString.append(BLACK_BOARDER_SQUARE);
        headerString.append(RESET_BG_COLOR);
        headerString.append("\n");

        return headerString.toString();
    }

    private String gameBoard() {
        StringBuilder boardString = new StringBuilder();
        for (int i = iterationStart; i < iterationEnd || i > iterationEnd; i += iterationDirection) {
            boardString.append(boardRow(i));
        }

        return boardString.toString();
    }

    private String boardRow(int index) {
        String color = getSquareColor(index, stateManager.getTeamColor());

        StringBuilder rowString = new StringBuilder();
        rowString.append(square(SET_BG_COLOR_LIGHT_GREY, ROWS[index]));

        for (int i = iterationStart; i < iterationEnd || i > iterationEnd; i += iterationDirection) {
            var displayColor = color;
            var position = new ChessPosition(8 - index, i + 1);
            if (highlightedSpaces.contains(position)) {
                displayColor = highlight(displayColor);
            } else if (position.equals(focusPosition)) {
                displayColor = SET_BG_COLOR_YELLOW;
            }
            ChessPiece piece = stateManager.getGameState().getBoard().getPiece(position);
            if (piece == null) {
                rowString.append(square(displayColor, EMPTY));
            } else {
                var isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
                Map<ChessPiece.PieceType, String> pieceMapping = isWhite ? WHITE_PIECE_MAPPING : BLACK_PIECE_MAPPING;
                String pieceColor = isWhite ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_RED;

                rowString.append(square(displayColor, occupantString(pieceColor, pieceMapping.get(piece.getPieceType()))));
            }

            color = toggleColor(color);

        }
        rowString.append(SET_TEXT_COLOR_BLACK);
        rowString.append(square(SET_BG_COLOR_LIGHT_GREY, ROWS[index]));
        rowString.append(RESET_BG_COLOR);
        rowString.append("\n");

        return rowString.toString();
    }

    private static String square(String backgroundColor, String occupant) {
        return backgroundColor + occupant;
    }

    private String occupantString(String foregroundColor, String occupantChar) {
        return foregroundColor + occupantChar;
    }

    private String getSquareColor(int index, ChessGame.TeamColor playerTeam) {

        String whiteColor = SET_BG_COLOR_WHITE;
        String blackColor = SET_BG_COLOR_BLACK;

        if (playerTeam == ChessGame.TeamColor.WHITE) {
            return index % 2 == 1 ? blackColor : whiteColor;
        } else {
            return index % 2 == 0 ? blackColor : whiteColor;
        }
    }

    private String highlight(String color) {
        return switch (color) {
            case SET_BG_COLOR_WHITE -> SET_BG_COLOR_GREEN;
            case SET_BG_COLOR_BLACK -> SET_BG_COLOR_DARK_GREEN;
            default -> color;
        };
    }

    private String toggleColor(String color) {
        return switch (color) {
            case SET_BG_COLOR_GREEN, SET_BG_COLOR_WHITE -> SET_BG_COLOR_BLACK;
            case SET_BG_COLOR_DARK_GREEN, SET_BG_COLOR_BLACK -> SET_BG_COLOR_WHITE;
            default -> color;
        };
    }
}
