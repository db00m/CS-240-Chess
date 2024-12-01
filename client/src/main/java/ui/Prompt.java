package ui;

import client.StateManager;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class Prompt {

    private final StateManager stateManager;

    public Prompt(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public String toString() {
        String prompt = switch (stateManager.getCurrentState()) {
            case LOGGED_OUT -> "[LOGGED_OUT] >>> ";
            case LOGGED_IN -> "[LOGGED_IN] >>> ";
            case IN_GAME -> "[IN_GAME] >>> ";
            case OBSERVING -> "[OBSERVING] >>> ";
        };

        return SET_TEXT_COLOR_LIGHT_GREY + prompt + RESET_TEXT_COLOR;
    }
}
