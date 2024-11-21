package notifications;

import chess.ChessGame;
import serialize.ObjectSerializer;
import ui.ChessBoardUI;
import websocket.messages.ServerMessage;

public class NotificationHandler {

    ObjectSerializer serializer = new ObjectSerializer();

    public void notify(String message) {

        ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);

        ChessGame game = serverMessage.getChessGame();
        var boardUI = new ChessBoardUI(game.getBoard().getBoardMatrix(), ChessGame.TeamColor.WHITE);

        System.out.println(boardUI);
    }
}
