package notifications;

import chess.ChessGame;
import client.ServerFacade;
import client.StateManager;
import serialize.ObjectSerializer;
import ui.ChessBoardUI;
import ui.MessagePresenter;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;
import static ui.MessagePresenter.handleError;
import static ui.MessagePresenter.printColoredMessage;

public class NotificationHandler {

    private final ObjectSerializer serializer = new ObjectSerializer();
    private final StateManager stateManager;
    private final ChessBoardUI boardUI;

    public NotificationHandler(StateManager stateManager, ChessBoardUI boardUI) {
        this.stateManager = stateManager;
        this.boardUI = boardUI;
    }

    public void notify(String message) {
        ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> loadGame(serverMessage.getGame());
            case NOTIFICATION -> handleNotification(serverMessage.getMessage());
            case ERROR -> handleError(serverMessage.getErrorMessage());
            default -> System.out.println(message);
        }
    }

    public void loadGame(ChessGame game) {
        stateManager.setGameState(game);

        System.out.println(boardUI);
    }

    public void handleNotification(String message) {
        MessagePresenter.printColoredMessage(message, SET_TEXT_COLOR_YELLOW);
    }
}
