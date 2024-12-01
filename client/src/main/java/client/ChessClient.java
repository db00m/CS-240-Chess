package client;

import chess.ChessGame;
import commandprocessing.InGameCommandProcessor;
import commandprocessing.LoggedInCommandProcessor;
import commandprocessing.LoggedOutCommandProcessor;
import notifications.NotificationHandler;
import ui.ChessBoardUI;
import ui.MenuUI;
import ui.Prompt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChessClient {


    private final MenuUI menuUI;
    private final ChessBoardUI boardUI;

    //    private final WebSocketFacade webSocketFacade;

    private final Map<Integer, Integer> gameMapping = new HashMap<>();
    private String authToken;

    private final StateManager stateManager;

    private final Prompt prompt;

    private final LoggedInCommandProcessor loggedInProcessor;
    private final LoggedOutCommandProcessor loggedOutProcessor;
    private final InGameCommandProcessor inGameCommandProcessor;

    public ChessClient(String url) throws IOException {
        var serverFacade = new ServerFacade("http://" + url);

        stateManager = new StateManager(ClientState.LOGGED_OUT);
        menuUI = new MenuUI(stateManager);
        prompt = new Prompt(stateManager);

        var notificationHandler = new NotificationHandler(stateManager);
        var webSocketFacade = new WebSocketFacade("ws://" + url, notificationHandler);

        var initialBoardMatrix = new ChessGame().getBoard().getBoardMatrix();
        boardUI = new ChessBoardUI(initialBoardMatrix, ChessGame.TeamColor.WHITE);

        loggedInProcessor = new LoggedInCommandProcessor(serverFacade, webSocketFacade, stateManager);
        loggedOutProcessor = new LoggedOutCommandProcessor(serverFacade, stateManager);
        inGameCommandProcessor = new InGameCommandProcessor(webSocketFacade, stateManager);
    }

    void eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (cmd) {
            case "help" -> help();
            case "quit" -> quit();
            default -> processCommand(cmd, params);
        }
    }

    private void processCommand(String cmd, String[] params) {
        switch (stateManager.getCurrentState()) {
            case LOGGED_OUT -> loggedOutProcessor.process(cmd, params);
            case LOGGED_IN -> loggedInProcessor.process(cmd, params);
            case IN_GAME, OBSERVING -> inGameCommandProcessor.process(cmd, params);
        }
    }

    Prompt getPrompt() {
        return prompt;
    }

    void help() {
        System.out.println(menuUI);
    }

    private void quit() {
        if (stateManager.getCurrentState() == ClientState.LOGGED_IN) {
            loggedInProcessor.logout();
        }
    }
}
