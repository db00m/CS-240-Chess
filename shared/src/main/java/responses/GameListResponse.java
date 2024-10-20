package responses;

import models.ChessGameModel;

import java.util.Collection;

public record GameListResponse(Collection<ChessGameModel> games) {
}
