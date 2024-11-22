package chess;

import java.util.Collection;
import java.util.Collections;
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
    private ChessMove lastMoveMade = null;

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

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            ChessMove enPassantMove = enPassantMove(startPosition);
            if (enPassantMove != null) { uncheckedMoves.add(enPassantMove); }
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            uncheckedMoves.addAll(possibleCastleMoves(startPosition));
        }

        for (ChessMove move : uncheckedMoves) {
            ChessBoard testBoard = testMove(move);
            if (testBoard != null) {
                checkedMoves.add(move);
            }
        }

        return checkedMoves;
    }


    /**
     * Checks if En Passant is possible.  If it is possible, the method will return a ChessMove.  If En Passant is not
     * possible, null will be returned.
     * @return move of MoveType enPassant if move is possible, null otherwise.
     */
    private ChessMove enPassantMove(ChessPosition startPosition) {
        if (lastMoveMade == null) { return null; }

        ChessPosition lastMoveEndPosition = lastMoveMade.endPosition();

        if (lastMoveEndPosition.row() != startPosition.row() || Math.abs(startPosition.col() - lastMoveEndPosition.col()) != 1) {
            return null; // piece is not next to last move piece
        }

        ChessPiece lastMovedPiece = board.getPiece(lastMoveEndPosition);
        int rowsMoved = lastMoveMade.startPosition().row() - lastMoveMade.endPosition().row();

        if (lastMovedPiece.getPieceType() != ChessPiece.PieceType.PAWN || Math.abs(rowsMoved) < 2) {
            return null; // last move was not double pawn
        }

        int direction = lastMovedPiece.getTeamColor() == TeamColor.WHITE ? -1 : 1;

        var newPosition = new ChessPosition(lastMoveEndPosition.row() + direction, lastMoveEndPosition.col());
        return new ChessMove(startPosition, newPosition);
    }

    /**
     * Checks if castle is possible.  If it is possible, the method will return a Collection of ChessMoves.  If Castle is not
     * possible, an empty collection will be returned.
     * @return collection of all possible castle moves.
     */
    private Collection<ChessMove> possibleCastleMoves(ChessPosition startingPosition) {
        ChessPiece king = board.getPiece(startingPosition);

        if (king == null ||
                king.getPieceType() != ChessPiece.PieceType.KING ||
                king.hasMoved() ||
                !king.startingPosition(0).equals(startingPosition)) {
            return Collections.emptySet();
        }

        Set<ChessMove> castleMoves = new HashSet<>();

        ChessPiece rook = board.getPiece(new ChessPosition(startingPosition.row(), 1)); // Queen Side
        Set<ChessPosition> clearedSpaces = new HashSet<>();
        Collection<ChessPosition> enemyEndPositions = enemyEndPositions(king.getTeamColor());

        if (rook != null && !rook.hasMoved()) {
            for (int i = 2; i < 5; i++) {
                checkSpace(startingPosition, i, clearedSpaces);
            }

            if (clearedSpaces.size() == 3 && Collections.disjoint(enemyEndPositions, clearedSpaces)) {
                var newKingPosition = new ChessPosition(startingPosition.row(), 3);
                castleMoves.add(new ChessMove(startingPosition, newKingPosition));
            }
        }

        rook = board.getPiece(new ChessPosition(startingPosition.row(), 8)); // King Side
        clearedSpaces.clear();
        if (rook != null && !rook.hasMoved()) {
            for (int i = 7; i > 5; i--) {
                checkSpace(startingPosition, i, clearedSpaces);
            }

            if (clearedSpaces.size() == 2 && Collections.disjoint(enemyEndPositions, clearedSpaces)) {
                var newKingPosition = new ChessPosition(startingPosition.row(), 7);
                castleMoves.add(new ChessMove(startingPosition, newKingPosition));
            }
        }

        return castleMoves;
    }

    private void checkSpace(ChessPosition startingPosition, int i, Set<ChessPosition> clearedSpaces) {
        var testPosition = new ChessPosition(startingPosition.row(), i);
        ChessPiece blockingPiece = board.getPiece(testPosition);
        if (blockingPiece == null) {
            clearedSpaces.add(testPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.startPosition());

        if (pieceToMove == null
                || pieceToMove.getTeamColor() != teamTurn
                || (!pieceToMove.pieceMoves(board, move.startPosition()).contains(move)
                && !isEnPassant(move)
                && !isCastle(move))) {
            throw new InvalidMoveException("Move is invalid");
        }

        ChessBoard newBoard = testMove(move);

        if (newBoard == null) {
            throw new InvalidMoveException("Move is invalid");
        }
        setBoard(newBoard);
        lastMoveMade = move;
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

        if (isEnPassant(move)) {
            board.handleEnPassant(move);
        } else if (isCastle(move)) {
            board.handleCastle(move);
        } else {
            board.movePiece(move);
        }

        if (isInCheck(pieceToMove.getTeamColor())) {
            setBoard(boardClone);
            return null;
        }

        ChessBoard resultBoard = board.clone();
        setBoard(boardClone);
        return resultBoard;
    }

    private boolean isEnPassant(ChessMove move) {
        if (move.startPosition().row() != 5 && move.startPosition().row() != 4) {
            return false;
        }

        ChessPiece pieceToMove = board.getPiece(move.startPosition());
        ChessPiece pieceToCapture = board.getPiece(move.endPosition());
        boolean isDiagonal = move.startPosition().col() != move.endPosition().col();

        return pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN && isDiagonal && pieceToCapture == null;
    }

    private boolean isCastle(ChessMove move) {
        ChessPiece pieceToMove = board.getPiece(move.startPosition());
        int distance = Math.abs(move.startPosition().col() - move.endPosition().col());

        return pieceToMove.getPieceType() == ChessPiece.PieceType.KING && distance > 1;
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

        return hasNoValidMoves(teamColor);
    }

    private boolean hasNoValidMoves(TeamColor teamColor) {
        Collection<ChessPosition> teamPiecePositions = board.getTeamPiecePositions(teamColor);
        for (ChessPosition position : teamPiecePositions) {
            if (!validMoves(position).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        return hasNoValidMoves(teamColor);
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
