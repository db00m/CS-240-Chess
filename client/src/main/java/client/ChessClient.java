package client;

import chess.ChessGame;
import ui.ChessBoardUI;
import ui.MenuUI;
import static ui.EscapeSequences.*;


import java.io.IOException;
import java.util.Arrays;


public class ChessClient {

    String state = "logged_out";
    MenuUI menuUI = new MenuUI();
    ServerFacade serverFacade;
    ChessBoardUI boardUI = new ChessBoardUI(new ChessGame().getBoard().getBoardMatrix(), ChessGame.TeamColor.WHITE);

    public ChessClient(String url) {
        serverFacade = new ServerFacade(url);
    }

    void eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (state.equals("logged_out")) {
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
        if (state.equals("logged_in")) {
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

            // TODO: Attempt login
            setLoggedInState();

        } catch (InvalidParamsException e) {
            handleError(e.getMessage());
        }
    }

    private void setLoggedInState() {
        state = "logged_in";
        menuUI.setState(state);
        help();
    }

    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                serverFacade.register(params[0], params[1], params[2]);
                setLoggedInState();
            }
        } catch (InvalidParamsException | IOException e) {
            handleError(e.getMessage());
        }


    }

    // Post-login commands

    private void logout() {
        // TODO: Attempt logout through http
        state = "logged_out";
        menuUI.setState(state);
    }

    private void createGame(String[] params) {
        // TODO: Attempt game creation
        // TODO: Print success or error message
    }

    private void listGames() {
        // TODO: Get games through Http
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
                message +
                RESET_TEXT_COLOR
        );
    }

}
