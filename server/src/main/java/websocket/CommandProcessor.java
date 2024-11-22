package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.ChessGameModel;
import models.UserModel;
import org.eclipse.jetty.websocket.api.Session;
import serialize.ObjectSerializer;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

public class CommandProcessor {

    GameService gameService = new GameService();
    Map<Integer, Set<Session>> sessions = new HashMap<>();
    ObjectSerializer serializer = new ObjectSerializer();

    public CommandProcessor() throws DataAccessException {
    }

    public void eval(UserGameCommand command, String username, Session session) throws DataAccessException, IOException {
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, username, session);
//            case MAKE_MOVE -> null;
//            case LEAVE -> null;
//            case RESIGN -> null;
        };
    }

    private void connect(UserGameCommand command, String username, Session session) throws DataAccessException, IOException {
        ChessGameModel gameModel = gameService.getGame(command.getGameID());
        String callerRole = gameModel.getUserRoll(username);

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                null,
                username + " joined game as " + callerRole
        );

        Set<Session> gameMembers = getConnectedSessions(command.getGameID(), session);
        gameMembers.add(session);
        for (Session memberSession : gameMembers) {
            if (memberSession != session) {
                memberSession.getRemote().sendString(serializer.toJson(notification));
            }
        }

        var loadMessage = new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME,
                gameModel.getGame(),
                null
        );

        session.getRemote().sendString(serializer.toJson(loadMessage));
    }

    private Set<Session> getConnectedSessions(int gameId, Session session) {
        return sessions.computeIfAbsent(gameId, k -> new HashSet<>());
    }


}
