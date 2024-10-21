package models;

import java.util.Objects;

public class ChessGameModel {
    private final int gameID;
    private final String gameName;
    private String whiteUsername;
    private String blackUsername;

    public ChessGameModel(int gameID, String gameName, String whiteUsername, String blackUsername) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

    public ChessGameModel(int gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
    }

    public void setWhiteUsername(String username) {
        this.whiteUsername = username;
    }

    public void setBlackUsername(String username) {
        this.blackUsername = username;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGameModel that = (ChessGameModel) o;
        return gameID == that.gameID && Objects.equals(gameName, that.gameName) && Objects.equals(whiteUsername, that.whiteUsername) && Objects.equals(blackUsername, that.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName, whiteUsername, blackUsername);
    }
}
