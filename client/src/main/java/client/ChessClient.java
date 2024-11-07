package client;

import static ui.EscapeSequences.*;

public class ChessClient {

    String state = "logged_out";

    void eval(String input) {
        // TODO: process commands
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

    private void create_game() {

    }

    private void play_game() {

    }

    private void observe_game() {

    }

}
