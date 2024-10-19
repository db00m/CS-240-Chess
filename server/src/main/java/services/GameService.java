package services;

import dataaccess.ChessGameDAO;
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
}
