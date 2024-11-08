package client;

import chess.ChessGame;

import java.io.IOException;
import java.net.URL;

public class ServerFacade {
    URL url;

    public ServerFacade(String urlString) throws IOException {
        url = new URL(urlString);
    }

    public ChessGame getGame(int _id) {
        return new ChessGame();
    }

    public void login(String username, String password) {

    }
}
