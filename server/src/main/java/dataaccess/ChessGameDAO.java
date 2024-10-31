package dataaccess;

import models.ChessGameModel;
import models.UserModel;

import java.util.Collection;

public interface ChessGameDAO {
    public int add(String gameName) throws DataAccessException;
    public ChessGameModel getById(int id) throws DataAccessException;
    public void setBlackUser(ChessGameModel game, UserModel user);
    public void setWhiteUser(ChessGameModel game, UserModel user);
    public Collection<ChessGameModel> getAll();
    public void clear();
}
