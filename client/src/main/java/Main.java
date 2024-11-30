import chess.*;
import client.ChessClient;
import client.Repl;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        String root = "localhost:8080";

        if (args.length > 0) {
            root = args[0];
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        new Repl(new ChessClient(root)).run();
    }
}