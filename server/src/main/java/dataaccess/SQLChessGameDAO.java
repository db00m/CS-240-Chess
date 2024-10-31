package dataaccess;

import chess.ChessGame;
import models.ChessGameModel;
import serialize.ObjectSerializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class SQLChessGameDAO implements ChessGameDAO {

    Connection conn = DatabaseManager.getConnection();

    public static String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS chess_games(
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                white_user_id INT,
                black_user_id INT,
                game_data VARCHAR(5000),
                PRIMARY KEY (id),
                FOREIGN KEY (white_user_id) REFERENCES users(id),
                FOREIGN KEY (black_user_id) REFERENCES users(id)
            );
            """;

    public SQLChessGameDAO() throws DataAccessException {
    }

    public static void main(String [] args) throws DataAccessException {
        var dao = new SQLChessGameDAO();
        dao.add("newGame");
    }

    @Override
    public int add(String gameName) throws DataAccessException {
        var serializer = new ObjectSerializer();
        String gameData = serializer.toJson(new ChessGame());
        var statement = "INSERT INTO chess_games (name, game_data) VALUES (?, ?)";

        try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, gameData);
            preparedStatement.executeUpdate();
            var rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new DataAccessException("Game was not created");
    }

    @Override
    public ChessGameModel getById(int id) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<ChessGameModel> getAll() {
        return List.of();
    }

    @Override
    public void clear() {

    }
}
