package client;

import java.util.Objects;
import java.util.Scanner;

public class Repl {
    public void run() {
        String result = "";
        Scanner scanner = new Scanner(System.in);


        System.out.println("This is a temp prompt"); // TODO: Put real prompt here
        do {

            result = scanner.nextLine();
        } while (!Objects.equals(result, "quit"));
    }

    private void printPrompt() {
        System.out.print("");
    }
}
