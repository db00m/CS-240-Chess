package dataaccess;

import models.ChessGameModel;

import java.util.Collection;
import java.util.List;

public class SQLChessGameDAO implements ChessGameDAO {

    public static String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS chess_games(
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                white_user_id INT,
                black_user_id INT,
                game_data VARCHAR(1000),
                PRIMARY KEY (id),
                FOREIGN KEY (white_user_id) REFERENCES users(id),
                FOREIGN KEY (black_user_id) REFERENCES users(id)
            );
            """;

    @Override
    public int add(String gameName) {
        return 0;
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
