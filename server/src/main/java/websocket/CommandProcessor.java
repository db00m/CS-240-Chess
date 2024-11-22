package websocket;

import dataaccess.DataAccessException;
import models.ChessGameModel;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.*;

public class CommandProcessor {

    GameService gameService = new GameService();
    Map<Integer, Set<Session>> sessions = new HashMap<>();
    MessageSender sender = new MessageSender();

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

        Set<Session> gameMembers = getConnectedSessions(command.getGameID(), session);
        gameMembers.add(session);
        sender.sendLoadGameNotification(session, gameModel.getGame());
        sender.sendGroupNotification(session, gameMembers, username + " has joined as " + callerRole);
    }

    private Set<Session> getConnectedSessions(int gameId, Session session) {
        return sessions.computeIfAbsent(gameId, k -> new HashSet<>());
    }


}
