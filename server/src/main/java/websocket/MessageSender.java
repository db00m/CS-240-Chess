package websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import serialize.ObjectSerializer;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;

public class MessageSender {

    ObjectSerializer serializer = new ObjectSerializer();

    public void sendLoadGameNotification(Session session, ChessGame game) throws IOException {
        var loadMessage = new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME,
                game
        );

        session.getRemote().sendString(serializer.toJson(loadMessage));
    }

    public void sendGroupNotification(Session callerSession, Collection<Session> otherSessions, String message) throws IOException {
        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                message
        );

        for (Session session : otherSessions) {
            if (callerSession != session) {
                session.getRemote().sendString(serializer.toJson(notification));
            }
        }
    }
}
