package service;

import dataaccess.*;
import models.ChessGameModel;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DBServiceTest {

    static UserDAO userDAO;
    static ChessGameDAO gameDAO;
    static AuthTokenDAO authDAO;

    static {
        try {
            var conn = DatabaseManager.getConnection();
            userDAO = new SQLUserDAO(conn);
            gameDAO = new SQLChessGameDAO(conn);
            authDAO = new SQLAuthTokenDAO(conn);
        } catch(DataAccessException exc) {
            throw new RuntimeException(exc);
        }
    }

    static UserModel user = new UserModel("username", "password", "fake@email.com");
    static ChessGameModel game = new ChessGameModel(1, "testGame");
    static int gameId;
    static UUID token = UUID.randomUUID();

    static DBService service = new DBService();

    @BeforeAll
    public static void setup() throws DataAccessException {
        userDAO.add(user);
        authDAO.add(token, fetchUser());
        gameId = gameDAO.add("testGame");
    }

    @AfterAll
    public static void clear() {
        authDAO.clearTokens();
        gameDAO.clear();
        userDAO.clearTable();
    }

    @Test
    public void standardClear() throws DataAccessException {
        Assertions.assertEquals(fetchUser().email(), user.email());
        Assertions.assertEquals(gameDAO.getById(gameId).getGameName(), game.getGameName());
        Assertions.assertEquals(authDAO.getUserByToken(token).email(), user.email());

        service.clearDB();

        Assertions.assertNull(userDAO.getUserByUsername("username"));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getById(1));
        Assertions.assertNull(authDAO.getUserByToken(token));
    }

    @Test
    public void emptyClear() {
        assertDoesNotThrow(() -> service.clearDB());
    }

    private static UserModel fetchUser() {
        return userDAO.getUserByUsername("username");
    }
}