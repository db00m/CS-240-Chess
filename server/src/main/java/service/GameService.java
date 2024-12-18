package service;

import chess.ChessGame;
import dataaccess.*;
import models.ChessGameModel;
import models.UserModel;

import java.util.Collection;

public class GameService {

    private final ChessGameDAO chessGameDAO = new SQLChessGameDAO();
    private final UserDAO userDAO = new SQLUserDAO();

    public GameService() throws DataAccessException {
    }

    public Collection<ChessGameModel> listGames() {
        return chessGameDAO.getAll();
    }

    public int createGame(String gameName) throws DataAccessException {
        return chessGameDAO.add(gameName);
    }

    public void joinGame(int gameId, String username, ChessGame.TeamColor playerColor) throws ValidationException, DataAccessException {
        ChessGameModel game = chessGameDAO.getById(gameId);
        UserModel user = userDAO.getUserByUsername(username);
        if (playerColor == ChessGame.TeamColor.BLACK) {
            setBlackPlayer(user, game);
        } else {
            setWhitePlayer(user, game);
        }
    }

    public void updateGame(ChessGameModel gameModel) {
        chessGameDAO.updateGame(gameModel);
    }

    public ChessGameModel getGame(int gameId) throws DataAccessException {
        return chessGameDAO.getById(gameId);
    }

    private void setBlackPlayer(UserModel user, ChessGameModel game) throws ValidationException {
        if (game.getBlackUsername() != null) {
            throw new ValidationException("selected team already has a player assigned");
        }

        chessGameDAO.setBlackUser(game, user);
    }

    private void setWhitePlayer(UserModel user, ChessGameModel game) {
        if (game.getWhiteUsername() != null) {
            throw new ValidationException("selected team already has a player assigned");
        }

        chessGameDAO.setWhiteUser(game, user);
    }
}
