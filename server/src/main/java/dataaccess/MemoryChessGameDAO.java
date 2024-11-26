package dataaccess;

import models.ChessGameModel;
import models.UserModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryChessGameDAO implements ChessGameDAO {

    private static final Map<Integer, ChessGameModel> GAMES_TABLE = new HashMap<>();
    private static int lastID = 0;

    @Override
    public int add(String gameName) {
        lastID++;
        GAMES_TABLE.put(lastID, new ChessGameModel(lastID, gameName));
        return lastID;
    }

    @Override
    public ChessGameModel getById(int id) throws DataAccessException {
        ChessGameModel game = GAMES_TABLE.getOrDefault(id, null);
        if (game == null) {
            throw new DataAccessException("Game does not exist");
        }

        return game;
    }

    @Override
    public void setBlackUser(ChessGameModel game, UserModel user) {
        game.setBlackUsername(user.username());
    }

    @Override
    public void setWhiteUser(ChessGameModel game, UserModel user) {
        game.setWhiteUsername(user.username());
    }

    @Override
    public void updateGame(ChessGameModel game) {

    }

    @Override
    public Collection<ChessGameModel> getAll() {
        return GAMES_TABLE.values();
    }

    @Override
    public void clear() {
        GAMES_TABLE.clear();
        lastID = 0;
    }
}
