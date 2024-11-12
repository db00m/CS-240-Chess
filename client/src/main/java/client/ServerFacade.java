package client;

import chess.ChessGame;
import requests.RegisterRequest;
import serialize.ObjectSerializer;
import serverconnection.ConnectionManager;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;


public class ServerFacade {
    ConnectionManager connectionManager;
    ObjectSerializer serializer = new ObjectSerializer();

    public ServerFacade(String urlString) {
        connectionManager = new ConnectionManager(urlString);
    }

    public ChessGame getGame(int _id) {
        return new ChessGame();
    }

    public void register(String username, String password, String email) throws IOException {
        var request = new RegisterRequest(username, password, email);
        String requestBody = serializer.toJson(request);

        connectionManager.doPost("/user", requestBody, Collections.emptyMap());
    }

    public void login(String username, String password) {

    }
}
