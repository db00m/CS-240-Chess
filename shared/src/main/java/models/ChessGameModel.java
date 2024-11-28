package models;

import chess.ChessGame;

import java.util.Objects;

public class ChessGameModel {
    private final int gameID;
    private final String gameName;
    private ChessGame game;
    private String whiteUsername;
    private String blackUsername;

    public ChessGameModel(int gameID, String gameName, ChessGame game, String whiteUsername, String blackUsername) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.game = game;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

    public ChessGameModel(int gameID, String gameName, String whiteUsername, String blackUsername) {
        this(gameID, gameName, null, whiteUsername, blackUsername);
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

    public int getID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getUserRoll(String username) {
        if (username.equals(whiteUsername)) {
            return "White";
        } else if (username.equals(blackUsername)) {
            return "Black";
        }

        return "Observer";
    }

    public ChessGame.TeamColor getUserTeam(String username) {
        if (whiteUsername.equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (blackUsername.equals(username)) {
            return ChessGame.TeamColor.BLACK;
        }

        return null;
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
        return gameID == that.gameID && Objects.equals(gameName, that.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName);
    }

    @Override
    public String toString() {
        String whiteUsernameString = whiteUsername;
        String blackUsernameString = blackUsername;

        if (whiteUsernameString == null) {
            whiteUsernameString = "<AVAILABLE>";
        }

        if (blackUsernameString == null) {
            blackUsernameString = "<AVAILABLE>";
        }

        return gameName + ":" +
                "\n\t WHITE -- " + whiteUsernameString +
                "\n\t BLACK -- " + blackUsernameString + "\n";
    }
}
