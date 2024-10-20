package requests;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, Integer gameID) {
    public void validate() throws InvalidRequestException {
        if ((gameID == null) || (playerColor == null)) {
            throw new InvalidRequestException();
        }
    }
}
