package dataaccess;

import models.UserModel;
import org.mindrot.jbcrypt.BCrypt;
import service.ValidationException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    private final Connection conn = DatabaseManager.getConnection();

    public static String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(225) NOT NULL,
                password VARCHAR(225) NOT NULL,
                email VARCHAR(225) NOT NULL,
                PRIMARY KEY (id)
            );
            """;

    public static void main(String [] args) throws DataAccessException {
        var dao = new SQLUserDAO();

        dao.clearTable();
    }

    public SQLUserDAO() throws DataAccessException {
    }

    @Override
    public void add(UserModel user) {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserModel getUserByUsername(String username) {
        var query = "SELECT username, password, email FROM users WHERE username = ?";
        try(var preparedQuery = conn.prepareStatement(query)) {
            preparedQuery.setString(1, username);
            try(var rs = preparedQuery.executeQuery()) {
                if (rs.next()) {
                    return new UserModel(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void validatePassword(UserModel user, String password) {
        if (user == null || !BCrypt.checkpw(password, user.getPassword())){
            throw new ValidationException("Invalid login credentials");
        }
    }

    @Override
    public void clearTable() {
        var statement = "DELETE FROM users";

        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }
}
