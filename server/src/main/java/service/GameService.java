package service;

import chess.ChessGame;
import dataaccess.ChessGameDAO;
import dataaccess.DataAccessException;
import dataaccess.memorydao.MemoryChessGameDAO;
import models.ChessGameModel;

import java.util.Collection;

public class GameService {

    private final ChessGameDAO chessGameDAO = new MemoryChessGameDAO();

    public Collection<ChessGameModel> listGames() {
        return chessGameDAO.getAll();
    }

    public int createGame(String gameName) {
        return chessGameDAO.add(gameName);
    }

    public void joinGame(int gameId, String username, ChessGame.TeamColor playerColor) throws ValidationException, DataAccessException {
        ChessGameModel game = chessGameDAO.getById(gameId);
        if (playerColor == ChessGame.TeamColor.BLACK) {
            setBlackPlayer(username, game);
        } else {
            setWhitePlayer(username, game);
        }
    }

    private void setBlackPlayer(String username, ChessGameModel game) throws ValidationException {
        if (game.getBlackUsername() != null) {
            throw new ValidationException("selected team already has a player assigned");
        }

        game.setBlackUsername(username);
    }

    private void setWhitePlayer(String username, ChessGameModel game) {
        if (game.getWhiteUsername() != null) {
            throw new ValidationException("selected team already has a player assigned");
        }

        game.setWhiteUsername(username);
    }
}
