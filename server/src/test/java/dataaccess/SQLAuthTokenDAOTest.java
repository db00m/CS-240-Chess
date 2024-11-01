package dataaccess;

import models.UserModel;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthTokenDAOTest {

    static final SQLAuthTokenDAO authDAO;
    static final SQLUserDAO userDAO;

    private static final UUID TOKEN = UUID.randomUUID();

    static {
        try {
            authDAO = new SQLAuthTokenDAO();
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @AfterEach
    void clear() {
        authDAO.clearTokens();
        userDAO.clearTable();
    }

    @Test
    void addWithExistingUser() {
        addUser();

        assertDoesNotThrow(() -> {
            authDAO.add(TOKEN, getExistingUser());
        });
    }

    @Test
    void addWithoutExistingUser() {
        UserModel fakeUser = new UserModel(1, "username", "password", "email");

        assertThrows(RuntimeException.class, () -> authDAO.add(TOKEN, fakeUser));
    }

    @Test
    void getUserByTokenWithExistingToken() {
        addToken();

        assertTokenExistsInTable();
    }

    @Test
    void getUserByTokenWithoutExistingToken() {
        assertNull(authDAO.getUserByToken(TOKEN));
    }

    @Test
    void deleteWithExisingToken() {
        addToken();

        assertTokenExistsInTable();
        assertDoesNotThrow(() -> authDAO.delete(TOKEN));
        assertTokenIsNotInTable();
    }

    @Test
    void deleteWithoutExistingToken() {
        assertDoesNotThrow(() -> authDAO.delete(TOKEN));
    }

    @Test
    void clearTokensWithExistingToken() {
        addToken();

        assertTokenExistsInTable();
        assertDoesNotThrow(authDAO::clearTokens);
        assertTokenIsNotInTable();
    }

    @Test
    void clearTokensWithoutExisingToken() {
        assertDoesNotThrow(authDAO::clearTokens);
    }

    private void addUser() {
        userDAO.add(new UserModel("username", "password", "email"));
    }

    private UserModel getExistingUser() {
        return userDAO.getUserByUsername("username");
    }

    private void addToken() {
        addUser();
        authDAO.add(TOKEN, getExistingUser());
    }

    private void assertTokenExistsInTable() {
        assertEquals(authDAO.getUserByToken(TOKEN), getExistingUser());
    }

    private void assertTokenIsNotInTable() {
        assertNull(authDAO.getUserByToken(TOKEN));
    }
}