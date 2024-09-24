package chess.move_calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QueenMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;
    Set<ChessMove> moves = new HashSet<>();

    public QueenMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> calculate() {
        moves.addAll(new RookMoveCalculator(board, myPosition, teamColor).calculate());
        moves.addAll(new BishopMoveCalculator(board, myPosition, teamColor).calculate());

        return moves;
    }
}
