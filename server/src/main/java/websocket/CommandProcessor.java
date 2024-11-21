package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.ChessGameModel;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class CommandProcessor {

    GameService gameService = new GameService();

    public CommandProcessor() throws DataAccessException {
    }

    public ServerMessage eval(UserGameCommand command) {
        return switch (command.getCommandType()) {
            case CONNECT -> connect(command);
            case MAKE_MOVE -> null;
            case LEAVE -> null;
            case RESIGN -> null;
        };
    }

    private ServerMessage connect(UserGameCommand command) {
        try {
            ChessGameModel gameModel = gameService.getGame(command.getGameID());
            return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameModel.getGame());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
