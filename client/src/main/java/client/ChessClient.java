package client;

import chess.ChessGame;
import models.ChessGameModel;
import ui.ChessBoardUI;
import ui.MenuUI;
import ui.MessagePresenter;

import static ui.EscapeSequences.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ChessClient {


    private final MenuUI menuUI = new MenuUI();
    private final ChessBoardUI boardUI = new ChessBoardUI(new ChessGame().getBoard().getBoardMatrix(), ChessGame.TeamColor.WHITE);

    private final ServerFacade serverFacade;
//    private final WebSocketFacade webSocketFacade;

    private final Map<Integer, Integer> gameMapping = new HashMap<>();
    private String authToken;

    private ClientState state = ClientState.LOGGED_OUT;

    public ChessClient(String url) throws IOException {
        serverFacade = new ServerFacade(url);
//        webSocketFacade = new WebSocketFacade(url);
    }

    void eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (authToken == null) {
            processLoggedOutCommand(cmd, params);
        } else {
            processLoggedInCommand(cmd, params);
        }
    }

    private void processLoggedOutCommand(String cmd, String[] params) {
        switch (cmd) {
            case "help" -> help();
            case "quit" -> quit();
            case "login" -> login(params);
            case "register" -> register(params);
            default -> MessagePresenter.handleInvalidCommand();
        }
    }

    private void processLoggedInCommand(String cmd, String[] params) {
        switch (cmd) {
            case "help" -> help();
            case "quit" -> quit();
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> playGame(params);
            case "observe" -> observeGame(params);
            default -> MessagePresenter.handleInvalidCommand();
        }
    }

    private void processObservingCommand(String cmd, String[] params) {

    }

    private void processInGameCommand(String cmd, String[] params) {

    }

    void printPrompt() {
        String prompt = switch (state) {
            case LOGGED_OUT -> "[LOGGED_OUT] >>> ";
            case LOGGED_IN -> "[LOGGED_IN] >>> ";
            default -> "not implemented";
        };


        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);
        System.out.print(prompt);
        System.out.print(RESET_TEXT_COLOR);
    }

    // universal commands

    void help() {
        System.out.println(menuUI);
    }

    // Pre-login commands

    private void quit() {
        if (authToken != null) {
            logout();
        }
    }

    private void login(String[] params) {
        MessagePresenter.printStatusMessage("Logging you in...");

        try {
            if (params.length < 2) {
                throw new InvalidParamsException("Username and password are required for login");
            }

            String token = serverFacade.login(params[0], params[1]);
            setState(ClientState.LOGGED_IN, token);

        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private void setState(ClientState state, String authToken) {
        MessagePresenter.printSuccessMessage("Success!");

        this.authToken = authToken;
        this.setState(state);
    }

    private void setState(ClientState state) {
        this.state = state;
        menuUI.setState(state);
    }

    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                MessagePresenter.printStatusMessage("Processing your registration...");
                String token = serverFacade.register(params[0], params[1], params[2]);
                setState(ClientState.LOGGED_IN, token);
            }
        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }


    }

    // Post-login commands

    private void logout() {
        MessagePresenter.printStatusMessage("Logging you out...");

        try {
            serverFacade.logout(authToken);
            setState(ClientState.LOGGED_OUT, null);
        } catch (IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private void createGame(String[] params) {
        try {
            if (params.length < 1) {
                throw new InvalidParamsException("Game name is required");
            }
            MessagePresenter.printStatusMessage("Creating game...");

            String gameName = String.join(" ", params);

            serverFacade.createGame(authToken, gameName);
            MessagePresenter.printSuccessMessage(gameName + " successfully created!");
        } catch (IOException | InvalidParamsException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private void listGames() {
        try {
            Collection<ChessGameModel> gameList = serverFacade.listGames(authToken);

            gameMapping.clear();

            int count = 1;
            for (ChessGameModel game : gameList) {
                gameMapping.put(count, game.getID());
                System.out.println(count + ". " + game);
                count ++;
            }
        } catch (IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private void playGame(String[] params) {
        try {

            if (params.length < 2) {
                throw new InvalidParamsException("Game ID and team color (BLACK|WHITE) are required");
            } else if (!params[1].equalsIgnoreCase("black") &&
                    !params[1].equalsIgnoreCase("white")) {
                throw new InvalidParamsException("Team color must be 'BLACK' or 'WHITE'");
            }

            ChessGame.TeamColor color = params[1].equalsIgnoreCase("black") ?
                    ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            Integer gameID = gameMapping.get(Integer.parseInt(params[0]));

            if(gameID == null) {
                throw new InvalidParamsException("Game with an ID of: " + params[0] + " does not exist");
            }

            serverFacade.joinGame(authToken, color, gameID);

            boardUI.setPlayerTeam(color);
            System.out.println(boardUI);

        } catch (IOException | InvalidParamsException e) {
            MessagePresenter.handleError(e.getMessage());
        }

    }

    private void observeGame(String[] params) {
        boardUI.setPlayerTeam(ChessGame.TeamColor.WHITE);
        System.out.println(boardUI);
    }
}
