package client;

import chess.ChessGame;
import ui.MessagePresenter;

public class StateManager {

    private ClientState currentState;

    private String authToken = null;

    private ChessGame gameState = new ChessGame();
    private Integer gameID;
    private ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;

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

    public void setGameState(ChessGame game) {
        this.gameState = game;
    }

    public ChessGame getGameState() {
        return gameState;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }


    public void setTeamColor(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public ClientState getCurrentState() {
        return currentState;
    }

    public String getAuthToken() {
        return authToken;
    }
}
