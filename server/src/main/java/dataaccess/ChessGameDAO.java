package dataaccess;

import chess.ChessGame;
import models.ChessGameModel;
import models.UserModel;
import serialize.ObjectSerializer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public interface ChessGameDAO {
    public int add(String gameName) throws DataAccessException;
    public ChessGameModel getById(int id) throws DataAccessException;
    public void setBlackUser(ChessGameModel game, UserModel user);
    public void setWhiteUser(ChessGameModel game, UserModel user);
    public Collection<ChessGameModel> getAll();
    public void clear();

    public static ChessGameModel getChessGame(PreparedStatement preparedQuery) throws SQLException {
        var serializer = new ObjectSerializer();

        try (var rs = preparedQuery.executeQuery()) {
            if(rs.next()) {
                return new ChessGameModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        serializer.fromJson(rs.getString("game_data"), ChessGame.class),
                        rs.getString("white_username"),
                        rs.getString("black_username"));
            }
        }

        return null;
    }
}
