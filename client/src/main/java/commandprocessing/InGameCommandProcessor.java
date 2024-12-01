package commandprocessing;

import chess.ChessMove;
import client.*;
import ui.ChessBoardUI;
import ui.MessagePresenter;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InGameCommandProcessor {

    private final WebSocketFacade webSocketFacade;
    private final StateManager stateManager;
    private final CoordinateParser coordinateParser = new CoordinateParser();
    private final ChessBoardUI boardUI;

    public InGameCommandProcessor(WebSocketFacade webSocketFacade, StateManager stateManager, ChessBoardUI boardUI) {
        this.webSocketFacade = webSocketFacade;
        this.stateManager = stateManager;
        this.boardUI = boardUI;
    }

    public void process(String cmd, String[] params) {
        try {
            switch (cmd) {
                case "move" -> makeMove(params);
                case "redraw" -> redraw();
                case "resign" -> resign();
                case "leave" -> leave();
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

    public void redraw() {
        System.out.println(boardUI);
    }

    public void resign() throws IOException {
        webSocketFacade.resign(stateManager.getAuthToken(), stateManager.getGameID());
    }

    public void leave() throws IOException {
        webSocketFacade.leave(stateManager.getAuthToken(), stateManager.getGameID());
        stateManager.setState(ClientState.LOGGED_IN);
    }
}
