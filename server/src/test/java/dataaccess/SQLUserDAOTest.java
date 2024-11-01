package dataaccess;

import models.UserModel;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    static UserDAO userDAO;
    static ChessGameDAO gameDAO;
    static AuthTokenDAO authTokenDAO;
    private static final UserModel BASIC_USER = new UserModel("username", "password", "email");

    static {
        try {
            Connection conn = DatabaseManager.getConnection();
            userDAO = new SQLUserDAO(conn);
            gameDAO = new SQLChessGameDAO(conn);
            authTokenDAO = new SQLAuthTokenDAO(conn);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        gameDAO.clear();
        authTokenDAO.clearTokens();
        userDAO.clearTable();
    }

    @AfterEach
    void clearDB() {
        userDAO.clearTable();
    }

    @Test
    void addWithCorrectParams() {
        assertDoesNotThrow(() -> userDAO.add(BASIC_USER));
    }

    @Test
    void addWithIncorrectParams() {
        assertThrows(RuntimeException.class, () -> userDAO.add(new UserModel(null, null, null)));
    }

    @Test
    void getUserByUsernameWhenUserExists() {
        userDAO.add(BASIC_USER);
        assertDoesNotThrow(() -> userDAO.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void getUserByUsernameWhenUseDoesNotExist() {
        assertNull(userDAO.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void clearTableWhenPopulated() {
        userDAO.add(BASIC_USER);
        assertDoesNotThrow(() -> userDAO.clearTable());
        assertNull(userDAO.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void clearTableWhenEmpty() {
        assertDoesNotThrow(() -> userDAO.clearTable());
    }
}