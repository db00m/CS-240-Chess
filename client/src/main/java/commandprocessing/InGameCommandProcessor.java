package commandprocessing;

import chess.ChessMove;
import client.InvalidParamsException;
import client.ServerFacade;
import client.StateManager;
import client.WebSocketFacade;
import ui.MessagePresenter;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InGameCommandProcessor {

    private final WebSocketFacade webSocketFacade;
    private final StateManager stateManager;
    private final CoordinateParser coordinateParser = new CoordinateParser();

    public InGameCommandProcessor(WebSocketFacade webSocketFacade, StateManager stateManager) {
        this.webSocketFacade = webSocketFacade;
        this.stateManager = stateManager;
    }

    public void process(String cmd, String[] params) {
        try {
            switch (cmd) {
                case "move" -> makeMove(params);
            }
        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    public void makeMove(String[] params) throws InvalidParamsException, IOException {

        if (params.length < 2) {
            throw new InvalidParamsException("Please include a start and end position");
        }

        ChessMove move = coordinateParser.parseMove(params);
        webSocketFacade.makeMove(stateManager.getAuthToken(), stateManager.getGameID(), move);
    }
}
