package ui;

import static ui.EscapeSequences.*;
import client.ClientState;

public class MenuUI {

    ClientState state = ClientState.LOGGED_OUT;

    @Override
    public String toString() {
        return switch (state) {
            case LOGGED_OUT -> loggedOutMenu();
            case LOGGED_IN -> loggedInMenu();
            default -> "not implemented";
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


    private String menuItem(String commandName, String commandDetails) {
        return "\t" +
                SET_TEXT_COLOR_BLUE +
                commandName +
                SET_TEXT_COLOR_MAGENTA +
                " -- " + commandDetails + "\n";
    }

    public void setState(ClientState newState) {
        this.state = newState;
    }
}
