package services;

import dataaccess.ChessGameDAO;
import dataaccess.memorydao.MemoryChessGameDAO;
import models.ChessGameModel;

import java.util.Collection;

public class GameService {
    public Collection<ChessGameModel> listGames() {
        ChessGameDAO chessGameDAO = new MemoryChessGameDAO();
        return chessGameDAO.getAll();
    }
}
