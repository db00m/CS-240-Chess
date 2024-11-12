package client;

import chess.ChessGame;
import models.ChessGameModel;
import requests.*;
import responses.*;
import serialize.ObjectSerializer;
import serverconnection.ConnectionManager;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


public class ServerFacade {
    ConnectionManager connectionManager;
    ObjectSerializer serializer = new ObjectSerializer();

    public ServerFacade(String urlString) {
        connectionManager = new ConnectionManager(urlString);
    }

    public ChessGame getGame(int _id) {
        return new ChessGame();
    }

    public String register(String username, String password, String email) throws IOException {
        var request = new RegisterRequest(username, password, email);
        String requestBody = serializer.toJson(request);

        LoginResponse response = serializer.fromJson(
                connectionManager.doPost("/user", requestBody, Collections.emptyMap()), LoginResponse.class
        );

        return response.authToken().toString();
    }

    public String login(String username, String password) throws IOException {
        var request = new LoginRequest(username, password);
        String requestBody = serializer.toJson(request);

        LoginResponse response = serializer.fromJson(
                connectionManager.doPost("/session", requestBody, Collections.emptyMap()), LoginResponse.class
        );

        return response.authToken().toString();
    }

    public void logout(String token) throws IOException {
        Map<String, String> headers = Map.of("Authorization", token);

        connectionManager.doDelete("/session", headers);
    }

    public void createGame(String token, String name) throws IOException {
        Map<String, String> headers = Map.of("Authorization", token);

        String requestBody = serializer.toJson(new CreateGameRequest(name));

        connectionManager.doPost("/game", requestBody, headers);
    }

    public Collection<ChessGameModel> getGameList(String token) throws IOException {
        Map<String, String> headers = Map.of("Authorization", token);
        GameListResponse response = serializer.fromJson(
                connectionManager.doGet("/game", headers), GameListResponse.class
        );

        return response.games();
    }
}
