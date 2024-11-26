package dataaccess;

import chess.ChessGame;
import models.ChessGameModel;
import models.UserModel;
import serialize.ObjectSerializer;

import java.sql.*;
import java.util.*;

public class SQLChessGameDAO implements ChessGameDAO {

    private final Connection conn;
    private final ObjectSerializer serializer = new ObjectSerializer();

    //language=MySQL
    public static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS chess_games(
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                white_user_id INT,
                black_user_id INT,
                game_data TEXT,
                PRIMARY KEY (id),
                FOREIGN KEY (white_user_id) REFERENCES users(id),
                FOREIGN KEY (black_user_id) REFERENCES users(id)
            );
            """;

    public SQLChessGameDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public SQLChessGameDAO(Connection conn) throws DataAccessException {
        this.conn = conn;
    }

    @Override
    public int add(String gameName) throws DataAccessException {
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
        var query = """
                SELECT
                    chess_games.id,
                    chess_games.name,
                    chess_games.game_data,
                    white_user.username as white_username,
                    black_user.username as black_username
                FROM
                    chess_games
                LEFT JOIN
                    users white_user ON chess_games.white_user_id = white_user.id
                LEFT JOIN
                    users black_user ON chess_games.black_user_id = black_user.id
                WHERE
                    chess_games.id = ?
                """;
        try (var preparedQuery = conn.prepareStatement(query)) {
            preparedQuery.setInt(1, id);
            ChessGameModel game = ChessGameDAO.getChessGame(preparedQuery);
            if (game != null) {
                return game;
            }
        } catch(SQLException exc) {
            throw new RuntimeException(exc);
        }

        throw new DataAccessException("Chess Game not found with provided id");
    }

    @Override
    public void setBlackUser(ChessGameModel game, UserModel user) {
        var statement = "UPDATE chess_games SET black_user_id = ? WHERE id = ?";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setInt(1, user.id());
            preparedStatement.setInt(2, game.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void setWhiteUser(ChessGameModel game, UserModel user) {
        var statement = "UPDATE chess_games SET white_user_id = ? WHERE id = ?";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setInt(1, user.id());
            preparedStatement.setInt(2, game.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void updateGame(ChessGameModel gameModel) {
        var statement = """
                UPDATE chess_games SET game_data = ? WHERE id = ?
                """;
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, serializer.toJson(gameModel.getGame()));
            preparedStatement.setInt(2, gameModel.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public Collection<ChessGameModel> getAll() {
        var query = """
                SELECT
                    chess_games.id,
                    chess_games.name,
                    black_user.username AS black_username,
                    white_user.username AS white_username
                FROM
                    chess_games
                LEFT JOIN
                    users black_user ON chess_games.black_user_id = black_user.id
                LEFT JOIN
                    users white_user ON chess_games.white_user_id = white_user.id
                """;

        Set<ChessGameModel> games = new HashSet<>();
        try (var preparedQuery = conn.prepareStatement(query)) {
            try (var rs = preparedQuery.executeQuery()) {
                while(rs.next()) {
                    games.add(new ChessGameModel(rs.getInt("id"), rs.getString("name"),
                            rs.getString("white_username"), rs.getString("black_username")));
                }
            }
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }

        return games;
    }

    @Override
    public void clear() {
        try(var preparedStatement = conn.prepareStatement("DELETE FROM chess_games")) {
            preparedStatement.executeUpdate();
        } catch(SQLException exc) {
            throw new RuntimeException(exc);
        }
    }
}
