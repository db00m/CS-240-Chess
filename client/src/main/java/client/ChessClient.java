package client;

import ui.MenuUI;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ChessClient {

    String state = "logged_out";
    MenuUI menuUI = new MenuUI();

    void eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (state.equals("logged_out")) {
            switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "login" -> login();
                case "register" -> register();
            }
        } else {
            switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "logout" -> logout();
                case "create" -> createGame();
                case "list" -> listGames();
                case "join" -> playGame();
                case "observe" -> observeGame();
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
        System.out.println(menuUI.toString(state));
    }

    // Pre-login commands

    private void quit() {

    }

    private void login() {

    }

    private void register() {

    }

    // Post-login commands

    private void logout() {

    }

    private void createGame() {

    }

    private void listGames() {

    }

    private void playGame() {

    }

    private void observeGame() {

    }

}
