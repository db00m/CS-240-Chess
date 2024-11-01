package dataaccess;

import models.UserModel;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    static UserDAO dao;
    private static final UserModel BASIC_USER = new UserModel("username", "password", "email");

    static {
        try {
            dao = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        dao.clearTable();
    }

    @AfterEach
    void clearDB() {
        dao.clearTable();
    }

    @Test
    void addWithCorrectParams() {
        assertDoesNotThrow(() -> dao.add(BASIC_USER));
    }

    @Test
    void addWithIncorrectParams() {
        assertThrows(RuntimeException.class, () -> dao.add(new UserModel(null, null, null)));
    }

    @Test
    void getUserByUsernameWhenUserExists() {
        dao.add(BASIC_USER);
        assertDoesNotThrow(() -> dao.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void getUserByUsernameWhenUseDoesNotExist() {
        assertNull(dao.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void clearTableWhenPopulated() {
        dao.add(BASIC_USER);
        assertDoesNotThrow(() -> dao.clearTable());
        assertNull(dao.getUserByUsername(BASIC_USER.username()));
    }

    @Test
    void clearTableWhenEmpty() {
        assertDoesNotThrow(() -> dao.clearTable());
    }
}