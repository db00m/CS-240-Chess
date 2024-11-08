import chess.*;
import client.Repl;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        new Repl().run();
    }
}