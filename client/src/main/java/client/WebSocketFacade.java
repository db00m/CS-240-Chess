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

    public WebSocketFacade(String urlString) {
        connectionManager = new WebSocketConnectionManager(urlString);
    }

    public void connect(String authToken, int gameID) throws IOException {
        var handler = new NotificationHandler();
        connectionManager.openSocket(handler);

        var connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        connectionManager.sendMessage(serializer.toJson(connectCommand));
    }

    public void makeMove(String authToken, int gameID, ChessMove requestedMove) throws IOException {
        var moveCommand = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, requestedMove);
    }

    public void resign(String authToken) throws IOException {

    }

    public void leave(String authToken) throws IOException {

    }
}
