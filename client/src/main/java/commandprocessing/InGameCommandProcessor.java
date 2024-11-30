package commandprocessing;

import client.ServerFacade;
import client.StateManager;
import client.WebSocketFacade;
import websocket.commands.UserGameCommand;

public class InGameCommandProcessor {

    private final WebSocketFacade webSocketFacade;
    private final StateManager stateManager;

    public InGameCommandProcessor(WebSocketFacade webSocketFacade, StateManager stateManager) {
        this.webSocketFacade = webSocketFacade;
        this.stateManager = stateManager;
    }

    public void process(String cmd, String[] params) {

    }
}
