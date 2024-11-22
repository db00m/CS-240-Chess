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

import java.io.IOException;
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
            processor.eval(command, user.username(), session);

        } catch (UnauthorizedException | DataAccessException e) {
            handleException(e, session);
        }

    }

    public void handleException(Exception e, Session session) throws IOException {
        session.getRemote().sendString(serializer.toJson(new ServerMessage(e)));
    }
}
