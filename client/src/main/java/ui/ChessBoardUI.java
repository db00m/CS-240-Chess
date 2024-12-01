package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.util.Map;

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

    private ChessPiece[][] boardState;
    private ChessGame.TeamColor playerTeam;
    private int iterationStart;
    private int iterationEnd;
    private int iterationDirection;

    public static void main(String [] args) {
        var board = new ChessBoard();
        board.resetBoard();
        var gui = new ChessBoardUI(board.getBoardMatrix(), ChessGame.TeamColor.WHITE);
        System.out.println(gui);
        gui.setPlayerTeam(ChessGame.TeamColor.BLACK);
        System.out.println(gui);
    }

    public ChessBoardUI(ChessPiece[][] boardState, ChessGame.TeamColor playerTeam) {
        this.boardState = boardState;
        setPlayerTeam(playerTeam);
    }

    public ChessBoardUI() {
    }

    public void setBoardState(ChessPiece[][] newBoardState) {
        boardState = newBoardState;
    }

    public void setPlayerTeam(ChessGame.TeamColor newPlayerTeam) {
        this.playerTeam = newPlayerTeam;
        if (playerTeam == ChessGame.TeamColor.BLACK) {
            iterationStart = 7;
            iterationEnd = -1;
            iterationDirection = -1;
        } else {
            iterationStart = 0;
            iterationEnd = 8;
            iterationDirection = 1;
        }
    }

    @Override
    public String toString() {
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
        String color = getSquareColor(index, playerTeam);

        StringBuilder rowString = new StringBuilder();
        rowString.append(square(SET_BG_COLOR_LIGHT_GREY, ROWS[index]));

        for (int i = iterationStart; i < iterationEnd || i > iterationEnd; i += iterationDirection) {
//            color = highlight(color);
            ChessPiece piece = boardState[7 - index][i];
            if (piece == null) {
                rowString.append(square(color, EMPTY));
            } else {
                var isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
                Map<ChessPiece.PieceType, String> pieceMapping = isWhite ? WHITE_PIECE_MAPPING : BLACK_PIECE_MAPPING;
                String pieceColor = isWhite ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_RED;

                rowString.append(square(color, occupantString(pieceColor, pieceMapping.get(piece.getPieceType()))));
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
