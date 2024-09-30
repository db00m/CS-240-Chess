package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Set<ChessMove> checkedMoves = new HashSet<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) { return checkedMoves; }

        Collection<ChessMove> uncheckedMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : uncheckedMoves) {
            ChessBoard testBoard = testMove(move);
            if (testBoard != null) {
                checkedMoves.add(move);
            }
        }

        return checkedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.startPosition());

        if (pieceToMove == null || pieceToMove.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Move is out of turn");
        }

        ChessBoard newBoard = testMove(move);

        if (newBoard == null) {
            throw new InvalidMoveException("Move is invalid");
        }
        setBoard(newBoard);
        setTeamTurn(getEnemyColor(teamTurn));
    }

    /**
     * Clones board and tests move.  If move is invalid, the method will throw an exception. Board is set to
     * original at end of method (no impact on actual game).
     * @param move move to test.
     * @return board with test move preformed, or null if move is invalid.
     */
    private ChessBoard testMove(ChessMove move) {
        var boardClone = board.clone();
        ChessPiece pieceToMove = board.getPiece(move.startPosition());

        if (pieceToMove == null || !pieceToMove.pieceMoves(board, move.startPosition()).contains(move)) {
            return null;
        }

        // TODO: How can I detect if move is castle or en passant?

        if (move.promotionPiece() != null) {
            pieceToMove = new ChessPiece(pieceToMove.getTeamColor(), move.promotionPiece());
        }

        board.addPiece(move.startPosition(), null);
        board.addPiece(move.endPosition(), pieceToMove); // capture occurs automatically

        if (isInCheck(pieceToMove.getTeamColor())) {
            setBoard(boardClone);
            return null;
        }

        ChessBoard resultBoard = board.clone();
        setBoard(boardClone);
        return resultBoard;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition teamKingPosition = board.getTeamKingPosition(teamColor);
        if (teamKingPosition == null) { return false; }
        return enemyEndPositions(teamColor).contains(teamKingPosition);
    }

    private Collection<ChessPosition> enemyEndPositions(TeamColor teamColor) {
        Set<ChessPosition> endPositions = new HashSet<>();
        Collection<ChessPosition> startPositions = board.getTeamPiecePositions(getEnemyColor(teamColor));

        for (ChessPosition startPosition : startPositions) {
            ChessPiece piece = board.getPiece(startPosition);
            if (piece == null) { continue; }

            Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
            for (ChessMove move : pieceMoves) {
                endPositions.add(move.getEndPosition());
            }
        }

        return endPositions;
    }

    private TeamColor getEnemyColor(TeamColor teamColor) {
        return teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessPosition> teamPiecePositions = board.getTeamPiecePositions(teamColor);
        for (ChessPosition position : teamPiecePositions) {
            return validMoves(position).isEmpty();
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // check if any piece of teamColor
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
