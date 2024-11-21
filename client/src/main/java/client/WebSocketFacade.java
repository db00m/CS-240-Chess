package client;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    private Session session = null;
    private URI uri;

    public WebSocketFacade(String urlString) {
        try {
            uri = new URI(urlString + "/ws");

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws IOException {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                // TODO: Replace with notification handler
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (DeploymentException e) {
            throw new IOException(e);
        }

    }

    public void send(String msg) throws IOException {
        if (this.session == null) {
            throw new IOException("Websocket has not been opened.  Call `connect` before send");
        }

        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
