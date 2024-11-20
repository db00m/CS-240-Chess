package client;

import chess.ChessGame;
import commandprocessing.LoggedInCommandProcessor;
import models.ChessGameModel;
import ui.ChessBoardUI;
import ui.MenuUI;
import ui.MessagePresenter;
import ui.Prompt;

import static ui.EscapeSequences.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ChessClient {


    private final MenuUI menuUI;
    private final ChessBoardUI boardUI;

    private final ServerFacade serverFacade;
//    private final WebSocketFacade webSocketFacade;

    private final Map<Integer, Integer> gameMapping = new HashMap<>();
    private String authToken;

    private final StateManager stateManager;

    private final Prompt prompt;

    private final LoggedInCommandProcessor loggedInProcessor;

    public ChessClient(String url) throws IOException {
        serverFacade = new ServerFacade(url);
        //        webSocketFacade = new WebSocketFacade(url);

        stateManager = new StateManager(ClientState.LOGGED_OUT);
        menuUI = new MenuUI(stateManager);
        prompt = new Prompt(stateManager);

        var initialBoardMatrix = new ChessGame().getBoard().getBoardMatrix();
        boardUI = new ChessBoardUI(initialBoardMatrix, ChessGame.TeamColor.WHITE);

        loggedInProcessor = new LoggedInCommandProcessor(serverFacade, stateManager);
    }

    void eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (cmd) {
            case "help" -> help();
            case "quit" -> quit();
            default -> {
                if (stateManager.getCurrentState() == ClientState.LOGGED_OUT) {
                    processLoggedOutCommand(cmd, params);
                } else {
                    processLoggedInCommand(cmd, params);
                }
            }
        }
    }

    private void processLoggedOutCommand(String cmd, String[] params) {
        switch (cmd) {
            case "login" -> login(params);
            case "register" -> register(params);
            default -> MessagePresenter.handleInvalidCommand();
        }
    }

    private void processLoggedInCommand(String cmd, String[] params) {
        loggedInProcessor.process(cmd, params);
    }

    private void processObservingCommand(String cmd, String[] params) {

    }

    private void processInGameCommand(String cmd, String[] params) {

    }

    Prompt getPrompt() {

        return prompt;
    }

    // universal commands

    void help() {
        System.out.println(menuUI);
    }

    // Pre-login commands

    private void quit() {
        if (stateManager.getCurrentState() == ClientState.LOGGED_IN) {
            loggedInProcessor.logout();
        }
    }

    private void login(String[] params) {
        MessagePresenter.printStatusMessage("Logging you in...");

        try {
            if (params.length < 2) {
                throw new InvalidParamsException("Username and password are required for login");
            }

            String token = serverFacade.login(params[0], params[1]);
            stateManager.setState(ClientState.LOGGED_IN, token);

        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }



    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                MessagePresenter.printStatusMessage("Processing your registration...");
                String token = serverFacade.register(params[0], params[1], params[2]);
                stateManager.setState(ClientState.LOGGED_IN, token);
            }
        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    // Post-login commands
}
