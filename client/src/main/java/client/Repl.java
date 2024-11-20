package client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(ChessClient client) throws IOException, URISyntaxException {
        this.client = client;
    }

    public void run() {
        String result;
        Scanner scanner = new Scanner(System.in);

        client.help();
        do {
            System.out.print(client.getPrompt());

            result = scanner.nextLine();
            client.eval(result);

        } while (!Objects.equals(result, "quit"));
    }
}
