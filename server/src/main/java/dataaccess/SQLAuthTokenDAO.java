package dataaccess;

import models.UserModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthTokenDAO implements AuthTokenDAO {

    public static String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS auth_tokens(
                token VARCHAR(40) NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (token),
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
            """;

    private final Connection conn;

    public SQLAuthTokenDAO() throws DataAccessException {
        this.conn = DatabaseManager.getConnection();
    }

    public SQLAuthTokenDAO(Connection conn) throws DataAccessException {
        this.conn = conn;
    }

    @Override
    public void add(UUID token, UserModel user) {
        var statement = "INSERT INTO auth_tokens VALUES (?, ?)";
        try {
            try (var prepareStatement = conn.prepareStatement(statement)) {
                prepareStatement.setString(1, token.toString());
                prepareStatement.setInt(2, user.ID());
                prepareStatement.executeUpdate();
            }
        } catch(SQLException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }

    @Override
    public UserModel getUserByToken(UUID authToken) {
        var statement = """
                SELECT users.id, users.username, users.email
                FROM auth_tokens
                LEFT JOIN users
                ON auth_tokens.user_id = users.id
                WHERE token = ?
                """;
        try {
            try (var prepareStatement = conn.prepareStatement(statement)) {
                prepareStatement.setString(1, authToken.toString());
                try (var rs = prepareStatement.executeQuery()) {
                    if (rs.next()) {
                        return new UserModel(rs.getInt("id"), rs.getString("username"), null, rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void delete(UUID authToken) {
        var statement = "DELETE FROM auth_tokens WHERE token = ?";

        try {
            try (var prepareStatement = conn.prepareStatement(statement)) {
                prepareStatement.setString(1, authToken.toString());
                prepareStatement.executeUpdate();
            }
        } catch(SQLException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }

    @Override
    public void clearTokens() {
        var statement = "DELETE FROM auth_tokens";

        try {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }
}
