package ui;

import static ui.EscapeSequences.*;
import client.ClientState;
import client.StateManager;

public class MenuUI {

    StateManager stateManager;

    public MenuUI(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public String toString() {
        return switch (stateManager.getCurrentState()) {
            case LOGGED_OUT -> loggedOutMenu();
            case LOGGED_IN -> loggedInMenu();
            case IN_GAME -> inGameMenu();
            case OBSERVING -> observingMenu();
        };
    }

    private String loggedOutMenu() {
        return menuItem("register <USERNAME> <PASSWORD> <EMAIL>", "create an account") +
                menuItem("login <USERNAME> <PASSWORD>", "start playing chess") +
                menuItem("quit", "to exit") +
                menuItem("help", "to show possible commands");
    }

    private String loggedInMenu() {
        return menuItem("create <NAME>", "a game") +
                menuItem("list", "all games") +
                menuItem("join <ID> [WHITE|BLACK]", "a game") +
                menuItem("observe <ID>", "a game") +
                menuItem("quit", "to exit") +
                menuItem("logout", "to login to a different account") +
                menuItem("help", "to show possible commands");
    }

    private String inGameMenu() {
        return menuItem("move <START_POSITION> <END_POSITION>", "a piece") +
                menuItem("redraw", "the game board") +
                menuItem("highlight <PIECE_POSITION>", "the possible moves of a piece") +
                menuItem("resign", "from the game") +
                menuItem("leave", "the game") +
                menuItem("help", "to show possible commands");
    }

    private String observingMenu() {
        return menuItem("redraw", "the game board") +
                menuItem("highlight <PIECE_POSITION>", "the possible moves of a piece") +
                menuItem("leave", "the game") +
                menuItem("help", "to show possible commands");
    }


    private String menuItem(String commandName, String commandDetails) {
        return "\t" +
                SET_TEXT_COLOR_BLUE +
                commandName +
                SET_TEXT_COLOR_MAGENTA +
                " -- " + commandDetails + "\n";
    }
}
