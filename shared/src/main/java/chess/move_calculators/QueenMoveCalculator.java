package chess.move_calculators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class QueenMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor teamColor;

    public QueenMoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> pieceMoves() {
        Set<ChessMove> moves = new HashSet<>();
        moves.addAll(new RookMoveCalculator(board, position, teamColor).pieceMoves());
        moves.addAll(new BishopMoveCalculator(board, position, teamColor).pieceMoves());

        return moves;
    }
}
