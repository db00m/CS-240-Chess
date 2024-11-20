package client;

import chess.ChessGame;
import models.ChessGameModel;
import ui.ChessBoardUI;
import ui.MenuUI;
import static ui.EscapeSequences.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ChessClient {

    private String authToken;
    private final MenuUI menuUI = new MenuUI();
    private final ServerFacade serverFacade;
    private final ChessBoardUI boardUI = new ChessBoardUI(new ChessGame().getBoard().getBoardMatrix(), ChessGame.TeamColor.WHITE);
    private final Map<Integer, Integer> gameMapping = new HashMap<>();

    public ChessClient(String url) {
        serverFacade = new ServerFacade(url);
    }

    void eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (authToken == null) {
            switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "login" -> login(params);
                case "register" -> register(params);
                default -> handleInvalidCommand();
            }
        } else {
            switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                default -> handleInvalidCommand();
            }
        }
    }

    public void printPrompt() {
        String prompt = "[LOGGED_OUT] >>> ";
        if (authToken != null) {
            prompt = "[LOGGED_IN] >>> ";
        }

        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);
        System.out.print(prompt);
        System.out.print(RESET_TEXT_COLOR);
    }

    // universal commands

    void help() {
        System.out.println(menuUI);
    }

    // Pre-login commands

    private void quit() {
        if (authToken != null) {
            logout();
        }
    }

    private void login(String[] params) {
        printStatusMessage("Logging you in...");

        try {
            if (params.length < 2) {
                throw new InvalidParamsException("Username and password are required for login");
            }

            String token = serverFacade.login(params[0], params[1]);
            setLoggedInState(token);

        } catch (InvalidParamsException | IOException e) {
            handleError(e.getMessage());
        }
    }

    private void setLoggedInState(String authToken) {
        printSuccessMessage("Success!");

        this.authToken = authToken;
        menuUI.setState("logged_in");
        help();
    }

    private void setLoggedOutState() {
        printSuccessMessage("Success!");

        this.authToken = null;
        menuUI.setState("logged_out");
    }

    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                printStatusMessage("Processing your registration...");
                String token = serverFacade.register(params[0], params[1], params[2]);
                setLoggedInState(token);
            }
        } catch (InvalidParamsException | IOException e) {
            handleError(e.getMessage());
        }


    }

    // Post-login commands

    private void logout() {
        printStatusMessage("Logging you out...");

        try {
            serverFacade.logout(authToken);
            setLoggedOutState();
        } catch (IOException e) {
            handleError(e.getMessage());
        }
    }

    private void createGame(String[] params) {
        try {
            if (params.length < 1) {
                throw new InvalidParamsException("Game name is required");
            }
            printStatusMessage("Creating game...");

            String gameName = String.join(" ", params);

            serverFacade.createGame(authToken, gameName);
            printSuccessMessage(gameName + " successfully created!");
        } catch (IOException | InvalidParamsException e) {
            handleError(e.getMessage());
        }
    }

    private void listGames() {
        try {
            Collection<ChessGameModel> gameList = serverFacade.listGames(authToken);

            gameMapping.clear();

            int count = 1;
            for (ChessGameModel game : gameList) {
                gameMapping.put(count, game.getID());
                System.out.println(count + ". " + game);
                count ++;
            }
        } catch (IOException e) {
            handleError(e.getMessage());
        }
    }

    private void playGame(String[] params) {
        try {

            if (params.length < 2) {
                throw new InvalidParamsException("Game ID and team color (BLACK|WHITE) are required");
            } else if (!params[1].equalsIgnoreCase("black") &&
                    !params[1].equalsIgnoreCase("white")) {
                throw new InvalidParamsException("Team color must be 'BLACK' or 'WHITE'");
            }

            ChessGame.TeamColor color = params[1].equalsIgnoreCase("black") ?
                    ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            Integer gameID = gameMapping.get(Integer.parseInt(params[0]));

            if(gameID == null) {
                throw new InvalidParamsException("Game with an ID of: " + params[0] + " does not exist");
            }

            serverFacade.joinGame(authToken, color, gameID);

            boardUI.setPlayerTeam(color);
            System.out.println(boardUI);

        } catch (IOException | InvalidParamsException e) {
            handleError(e.getMessage());
        }

    }

    private void observeGame(String[] params) {
        boardUI.setPlayerTeam(ChessGame.TeamColor.WHITE);
        System.out.println(boardUI);
        boardUI.setPlayerTeam(ChessGame.TeamColor.BLACK);
        System.out.println(boardUI);
    }

    private void handleInvalidCommand() {
        handleError("Command entered is not recognized, please enter a valid command");
        help();
    }

    private void handleError(String message) {
        printColoredMessage(message + ", please try again.", SET_TEXT_COLOR_RED);
    }

    private void printSuccessMessage(String message) {
        printColoredMessage(message, SET_TEXT_COLOR_GREEN);
    }

    private void printStatusMessage(String message) {
        printColoredMessage(message, SET_TEXT_COLOR_WHITE);
    }

    private void printColoredMessage(String message, String color) {
        System.out.println(
                color +
                message +
                RESET_TEXT_COLOR
        );
    }

}
