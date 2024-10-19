package responses;

import models.ChessGameModel;

import java.util.Collection;

public record GameListResponse(Collection<ChessGameModel> games, String message) {
    public GameListResponse(Collection<ChessGameModel> games) {
        this(games, null);
    }

    public GameListResponse(String message) {
        this(null, message);
    }
}
