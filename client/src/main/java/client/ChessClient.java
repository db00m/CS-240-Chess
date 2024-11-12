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
                default -> invalidCommand();
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
                default -> invalidCommand();
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
        System.out.println(menuUI.toString());
    }

    // Pre-login commands

    private void quit() {
        // TODO: logout player if logged in
        // TODO: print exit message
    }

    private void login(String[] params) {
        System.out.println(SET_TEXT_COLOR_WHITE + "Logging you in...");

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
        System.out.println(SET_TEXT_COLOR_GREEN + "Success!");

        this.authToken = authToken;
        menuUI.setState("logged_in");
        help();
    }

    private void setLoggedOutState() {
        System.out.println(SET_TEXT_COLOR_GREEN + "Success!");

        this.authToken = null;
        menuUI.setState("logged_out");
        help();
    }

    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                System.out.println(SET_TEXT_COLOR_WHITE + "Processing your registration...");
                String token = serverFacade.register(params[0], params[1], params[2]);
                setLoggedInState(token);
            }
        } catch (InvalidParamsException | IOException e) {
            handleError(e.getMessage());
        }


    }

    // Post-login commands

    private void logout() {
        System.out.println(SET_TEXT_COLOR_WHITE + "Logging you out...");

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

            String gameName = String.join(" ", params);

            serverFacade.createGame(authToken, gameName);
        } catch (IOException | InvalidParamsException e) {
            handleError(e.getMessage());
        }
        // TODO: Print success or error message
    }

    private void listGames() {
        try {
            Collection<ChessGameModel> gameList = serverFacade.getGameList(authToken);

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
        // TODO: Print game list

        // TODO: Might want to store game list, this way we can use UI ID's not DB ID's for game selection
    }

    private void playGame(String[] params) {
        ChessGame.TeamColor color = params[1].equalsIgnoreCase("black") ?
                ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        // TODO: Attempt to join game through HTTP as color requested in params
        // TODO: Get game by ID
        boardUI.setPlayerTeam(color);
        System.out.println(boardUI);
    }

    private void observeGame(String[] params) {
        // TODO: get game by ID provided by params
        System.out.println(boardUI);
    }

    private void invalidCommand() {
        System.out.println(
                SET_TEXT_COLOR_RED +
                SET_TEXT_BOLD +
                "Command entered is not recognized, please enter a valid command" +
                RESET_TEXT_COLOR +
                RESET_TEXT_BOLD_FAINT
        );

        help();
    }

    private void handleError(String message) {
        System.out.println(
                SET_TEXT_COLOR_RED +
                message + ", please try again." +
                RESET_TEXT_COLOR
        );
    }

}
