package ui;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class MessagePresenter {
    public static void handleInvalidCommand() {
        handleError("Command entered is not recognized, please enter a valid command");
    }

    public static void handleError(String message) {
        printColoredMessage(message + ", please try again.", SET_TEXT_COLOR_RED);
    }

    public static void printSuccessMessage(String message) {
        printColoredMessage(message, SET_TEXT_COLOR_GREEN);
    }

    public static void printStatusMessage(String message) {
        printColoredMessage(message, SET_TEXT_COLOR_WHITE);
    }

    public static void printColoredMessage(String message, String color) {
        System.out.println(
                color + message + RESET_TEXT_COLOR
        );
    }
}
