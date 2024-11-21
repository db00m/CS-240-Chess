package websocket;

import dataaccess.DataAccessException;
import models.UserModel;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import serialize.ObjectSerializer;
import service.AuthorizationService;
import service.UnauthorizedException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WSServer {

    private final Map<String, Session> sessions = new HashMap<>();
    private final ObjectSerializer serializer = new ObjectSerializer();
    private final CommandProcessor processor = new CommandProcessor();

    public WSServer() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
        try {
            UserModel user = AuthorizationService.authorize(command.getAuthToken());
            ServerMessage serverMessage = processor.eval(command);
            session.getRemote().sendString(serializer.toJson(serverMessage));

        } catch (UnauthorizedException | DataAccessException e) {
            session.getRemote().sendString(e.getMessage());
        }

    }
}
