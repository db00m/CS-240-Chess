package models;

public class ChessGameModel {
    private final int gameId;
    private final String gameName;
    private String whiteUsername;
    private String blackUsername;

    public ChessGameModel(int gameId, String gameName, String whiteUsername, String blackUsername) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

    public ChessGameModel(int gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public void setWhiteUsername(String username) {
        this.whiteUsername = username;
    }

    public void setBlackUsername(String username) {
        this.blackUsername = username;
    }

    public int getGameId() {
        return gameId;
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
}
