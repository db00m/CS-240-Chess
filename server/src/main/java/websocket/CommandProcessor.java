package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
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

    public void eval(UserGameCommand command, String username, Session session) throws DataAccessException, IOException, InvalidMoveException {
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, username, session);
            case MAKE_MOVE -> makeMove(command, username, session);
//            case LEAVE -> null;
            case RESIGN -> resign(command, username, session);
        };
    }

    private void connect(UserGameCommand command, String username, Session session)
            throws DataAccessException, IOException {
        ChessGameModel gameModel = getGameModel(command.getGameID());
        String callerRole = gameModel.getUserRoll(username);

        Set<Session> gameMembers = getConnectedSessions(command.getGameID(), session);
        gameMembers.add(session);
        sender.loadGameForOne(session, gameModel.getGame());
        sender.sendGroupNotification(session, gameMembers, username + " has joined as " + callerRole);
    }

    private void makeMove(UserGameCommand command, String username, Session session)
            throws DataAccessException, IOException, InvalidMoveException {
        ChessGameModel gameModel = getGameModel(command.getGameID());
        ChessGame game = gameModel.getGame();

        checkPlayerPresence(gameModel);

        if (game.getTeamTurn() != gameModel.getUserTeam(username)) {
            throw new InvalidMoveException("It is not your turn.");
        }

        game.makeMove(command.getMove());
        gameService.updateGame(gameModel);

        Set<Session> gameMembers = getConnectedSessions(command.getGameID(), session);
        sender.loadGameForAll(gameMembers, game);
        sender.sendGroupNotification(session, gameMembers, username + " moved.");
    }

    private void resign(UserGameCommand command, String username, Session session) throws DataAccessException, IOException, InvalidMoveException {
        ChessGameModel gameModel = getGameModel(command.getGameID());

        checkPlayerPresence(gameModel);

        switch (gameModel.getUserRoll(username)) {
            case "White" -> gameModel.setWhiteUsername(null);
            case "Black" -> gameModel.setBlackUsername(null);
            default -> throw new InvalidMoveException("Not a player");
        }

        gameService.updateGame(gameModel);

        Set<Session> gameMembers = getConnectedSessions(command.getGameID(), session);
        sender.sendGroupNotification(null, gameMembers, username + " resigned from the game");
        gameMembers.remove(session);
    }

    private Set<Session> getConnectedSessions(int gameId, Session session) {
        return sessions.computeIfAbsent(gameId, k -> new HashSet<>());
    }

    private ChessGameModel getGameModel(int gameID) throws DataAccessException {
        return gameService.getGame(gameID);
    }

    private void checkPlayerPresence(ChessGameModel gameModel) throws InvalidMoveException {
        if (gameModel.getWhiteUsername() == null || gameModel.getBlackUsername() == null) {
            throw new InvalidMoveException("Both player need to be present");
        }
    }

}
