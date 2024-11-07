package client;

import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ChessClient client = new ChessClient();

    public void run() {
        String result;
        Scanner scanner = new Scanner(System.in);


        do {
            client.printPrompt();

            result = scanner.nextLine();
            client.eval(result);

        } while (!Objects.equals(result, "quit"));
    }

    private void printPrompt() {
        System.out.print("");
    }
}
