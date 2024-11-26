package dataaccess;

import chess.ChessGame;
import models.ChessGameModel;
import models.UserModel;
import serialize.ObjectSerializer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public interface ChessGameDAO {
    int add(String gameName) throws DataAccessException;
    ChessGameModel getById(int id) throws DataAccessException;
    void setBlackUser(ChessGameModel game, UserModel user);
    void setWhiteUser(ChessGameModel game, UserModel user);
    void updateGame(ChessGameModel game);
    Collection<ChessGameModel> getAll();
    void clear();


    static ChessGameModel getChessGame(PreparedStatement preparedQuery) throws SQLException {
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
