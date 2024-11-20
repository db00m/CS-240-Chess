package websocket;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import java.util.HashSet;
import java.util.Set;

@WebSocket
public class WSServer {

    Set<Session> session = new HashSet<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
