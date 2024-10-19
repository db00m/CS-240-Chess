package dataaccess.memorydao;

import dataaccess.ChessGameDAO;
import dataaccess.DataAccessException;
import models.ChessGameModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryChessGameDAO implements ChessGameDAO {

    private static final Map<Integer, ChessGameModel> gamesTable = new HashMap<>();
    private static int lastID = 0;

    @Override
    public void add(ChessGameModel game) {
        lastID++;
        gamesTable.put(lastID, game);
    }

    @Override
    public ChessGameModel getById(int id) throws DataAccessException {
        ChessGameModel game = gamesTable.getOrDefault(id, null);
        if (game == null) {
            throw new DataAccessException("Game does not exist");
        }

        return game;
    }

    @Override
    public Collection<ChessGameModel> getAll() {
        return gamesTable.values();
    }

    @Override
    public void clear() {
        gamesTable.clear();
    }
}
