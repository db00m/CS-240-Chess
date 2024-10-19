package dataaccess;

import models.ChessGameModel;

import java.util.Collection;

public interface ChessGameDAO {
    public int add(String gameName);
    public ChessGameModel getById(int id) throws DataAccessException;
    public Collection<ChessGameModel> getAll();
    public void clear();
}