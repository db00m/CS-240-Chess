package client;

import ui.MessagePresenter;

public class StateManager {
    private ClientState currentState;

    private String authToken = null;

    StateManager(ClientState initialState) {
        currentState = initialState;
    }

    public void setState(ClientState state, String authToken) {
        setState(state);

        this.authToken = authToken;
    }

    public void setState(ClientState state) {
        MessagePresenter.printSuccessMessage("Success!");
        this.currentState = state;
    }

    void setCurrentState(ClientState newState) {
        currentState = newState;
    }

    public ClientState getCurrentState() {
        return currentState;
    }

    void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
