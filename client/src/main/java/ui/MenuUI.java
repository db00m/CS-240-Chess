package ui;

import static ui.EscapeSequences.*;

public class MenuUI {

    String state = "logged_out";

    @Override
    public String toString() {
        String menu;

        if (state.equals("logged_out")) {
            menu = menuItem("register <USERNAME> <PASSWORD> <EMAIL>", "create an account") +
                    menuItem("login <USERNAME> <PASSWORD>", "start playing chess") +
                    menuItem("quit", "to exit") +
                    menuItem("help", "to show possible commands");
        } else {
            menu = menuItem("create <NAME>", "a game") +
                    menuItem("list", "all games") +
                    menuItem("join <ID> [WHITE|BLACK]", "a game") +
                    menuItem("observe <ID>", "a game") +
                    menuItem("quit", "to exit") +
                    menuItem("logout", "to login to a different account") +
                    menuItem("help", "to show possible commands");
        }

        return menu;
    }

    private String menuItem(String commandName, String commandDetails) {
        return "\t" +
                SET_TEXT_COLOR_BLUE +
                commandName +
                SET_TEXT_COLOR_MAGENTA +
                " -- " + commandDetails + "\n";
    }

    public void setState(String newState) {
        this.state = newState;
    }
}
