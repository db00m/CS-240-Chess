package client;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ChessClient client = new ChessClient("http://localhost:8080/");

    public Repl() throws IOException {
    }

    public void run() {
        String result;
        Scanner scanner = new Scanner(System.in);

        client.help();
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
