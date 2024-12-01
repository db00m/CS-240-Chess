package client;

import chess.ChessMove;
import notifications.NotificationHandler;
import serialize.ObjectSerializer;
import serverconnection.WebSocketConnectionManager;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade {

    private final WebSocketConnectionManager connectionManager;
    private final ObjectSerializer serializer = new ObjectSerializer();
    private final NotificationHandler handler;

    public WebSocketFacade(String urlString, NotificationHandler handler) {
        connectionManager = new WebSocketConnectionManager(urlString);
        this.handler = handler;
    }

    public void connect(String authToken, int gameID) throws IOException {
        connectionManager.openSocket(handler);

        var connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        connectionManager.sendMessage(serializer.toJson(connectCommand));
    }

    public void makeMove(String authToken, int gameID, ChessMove requestedMove) throws IOException {
        var moveCommand = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, requestedMove);
        connectionManager.sendMessage(serializer.toJson(moveCommand));
    }

    public void resign(String authToken, int gameID) throws IOException {
        var resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        connectionManager.sendMessage(serializer.toJson(resignCommand));
    }

    public void leave(String authToken, int gameID) throws IOException {
        var leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        connectionManager.sendMessage(serializer.toJson(leaveCommand));
    }
}
