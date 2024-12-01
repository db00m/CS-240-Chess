package commandprocessing;

import chess.ChessGame;
import client.*;
import models.ChessGameModel;
import ui.MessagePresenter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LoggedInCommandProcessor {

    private final ServerFacade serverFacade;
    private final StateManager stateManager;
    private final WebSocketFacade webSocketFacade;

    private final Map<Integer, Integer> gameMapping = new HashMap<>();

    public LoggedInCommandProcessor(ServerFacade serverFacade, WebSocketFacade webSocketFacade, StateManager stateManager) {
        this.serverFacade = serverFacade;
        this.stateManager = stateManager;
        this.webSocketFacade = webSocketFacade;
    }

    public void process(String cmd, String[] params) {
        switch (cmd) {
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> playGame(params);
            case "observe" -> observeGame(params);
            default -> MessagePresenter.handleInvalidCommand();
        }
    }

    public void logout() {
        MessagePresenter.printStatusMessage("Logging you out...");

        try {
            serverFacade.logout(stateManager.getAuthToken());
            stateManager.setState(ClientState.LOGGED_OUT, null);
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

            serverFacade.createGame(stateManager.getAuthToken(), gameName);
            MessagePresenter.printSuccessMessage(gameName + " successfully created!");
        } catch (IOException | InvalidParamsException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private void listGames() {
        try {
            Collection<ChessGameModel> gameList = serverFacade.listGames(stateManager.getAuthToken());

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

            int gameID = getGameId(params[0]);

            serverFacade.joinGame(stateManager.getAuthToken(), color, gameID);
            webSocketFacade.connect(stateManager.getAuthToken(), gameID);

            stateManager.setState(ClientState.IN_GAME);
            stateManager.setTeamColor(color);
            stateManager.setGameID(gameID);
        } catch (IOException | InvalidParamsException e) {
            MessagePresenter.handleError(e.getMessage());
        }

    }

    private void observeGame(String[] params) {
        try {
            if (params.length < 1) {
                throw new InvalidParamsException("Game ID is required");
            }

            int gameID = getGameId(params[0]);

            webSocketFacade.connect(stateManager.getAuthToken(), gameID);

            stateManager.setState(ClientState.OBSERVING);
            stateManager.setGameID(gameID);
        } catch (InvalidParamsException | IOException e ) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

    private Integer getGameId(String gameIdParam) throws InvalidParamsException {
        Integer gameID = gameMapping.get(Integer.parseInt(gameIdParam));

        if(gameID == null) {
            throw new InvalidParamsException("Game with an ID of: " + gameIdParam + " does not exist");
        }

        return gameID;
    }
}
