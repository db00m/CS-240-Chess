package commandprocessing;

import client.ClientState;
import client.InvalidParamsException;
import client.ServerFacade;
import client.StateManager;
import ui.MessagePresenter;

import java.io.IOException;

public class LoggedOutCommandProcessor {

    private final ServerFacade serverFacade;
    private final StateManager stateManager;

    public LoggedOutCommandProcessor(ServerFacade serverFacade, StateManager stateManager) {
        this.serverFacade = serverFacade;
        this.stateManager = stateManager;
    }

    public void process(String cmd, String[] params) {
        switch (cmd) {
            case "login" -> login(params);
            case "register" -> register(params);
            default -> MessagePresenter.handleInvalidCommand();
        }
    }

    private void login(String[] params) {
        MessagePresenter.printStatusMessage("Logging you in...");

        try {
            if (params.length < 2) {
                throw new InvalidParamsException("Username and password are required for login");
            }

            String token = serverFacade.login(params[0], params[1]);
            stateManager.setState(ClientState.LOGGED_IN, token);

        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }



    private void register(String[] params) {
        try {
            if (params.length < 3) {
                throw new InvalidParamsException("Username, password, and email are required for registering.");
            } else {
                MessagePresenter.printStatusMessage("Processing your registration...");
                String token = serverFacade.register(params[0], params[1], params[2]);
                stateManager.setState(ClientState.LOGGED_IN, token);
            }
        } catch (InvalidParamsException | IOException e) {
            MessagePresenter.handleError(e.getMessage());
        }
    }

}
