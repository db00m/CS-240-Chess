package serverconnection;

import notifications.NotificationHandler;
import serialize.ObjectSerializer;
import websocket.commands.UserGameCommand.CommandType;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketConnectionManager extends Endpoint {
    private URI uri;
    private final static String PATH = "/ws";
    private Session session = null;

    public static void main(String [] args) throws IOException {
        var con = new WebSocketConnectionManager("ws://localhost:8080");
        con.openSocket(new NotificationHandler());
        UserGameCommand command = new UserGameCommand(CommandType.CONNECT, "2fbf4eea-12e5-4bce-a5ef-076ecfebd213", 484);
        con.sendMessage(new ObjectSerializer().toJson(command));
        while (true) {}
    }


    public WebSocketConnectionManager(String root) {
        try {
            uri = new URI(root + PATH);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void openSocket(NotificationHandler handler) throws IOException {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    handler.notify(message);
                }
            });
        } catch (DeploymentException e) {
            throw new IOException(e);
        }
    }

    public void sendMessage(String message) throws IOException {
        if (this.session == null) {
            throw new IOException("Websocket has not been opened.  Call `connect` before send");
        }

        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
