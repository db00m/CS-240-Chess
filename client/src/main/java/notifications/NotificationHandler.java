package notifications;

import chess.ChessGame;
import client.ServerFacade;
import client.StateManager;
import serialize.ObjectSerializer;
import ui.ChessBoardUI;
import websocket.messages.ServerMessage;

public class NotificationHandler {

    private final ObjectSerializer serializer = new ObjectSerializer();
    private final StateManager stateManager;
    private final ChessBoardUI boardUI;

    public NotificationHandler(StateManager stateManager, ChessBoardUI boardUI) {
        this.stateManager = stateManager;
        this.boardUI = boardUI;
    }

    public void notify(String message) {
        System.out.println();
        ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> loadGame(serverMessage.getGame());
            default -> System.out.println(message);
        }
    }

    public void loadGame(ChessGame game) {
        stateManager.setGameState(game);

        System.out.println(boardUI);
    }
}
