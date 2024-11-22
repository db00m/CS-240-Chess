package notifications;

import chess.ChessGame;
import serialize.ObjectSerializer;
import ui.ChessBoardUI;
import websocket.messages.ServerMessage;

public class NotificationHandler {

    ObjectSerializer serializer = new ObjectSerializer();

    public void notify(String message) {
        System.out.println(message);
    }
}
