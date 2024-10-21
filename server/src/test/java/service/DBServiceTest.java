package service;

import dataaccess.AuthTokenDAO;
import dataaccess.ChessGameDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.memorydao.MemoryAuthTokenDAO;
import dataaccess.memorydao.MemoryChessGameDAO;
import dataaccess.memorydao.MemoryUserDAO;
import models.ChessGameModel;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DBServiceTest {

    static UserDAO userDAO = new MemoryUserDAO();
    static ChessGameDAO gameDAO = new MemoryChessGameDAO();
    static AuthTokenDAO authDAO = new MemoryAuthTokenDAO();

    static UserModel user = new UserModel("username", "password", "fake@email.com");
    static ChessGameModel game = new ChessGameModel(1, "testGame");
    static UUID token = UUID.randomUUID();

    @BeforeAll
    public static void setup() {
        userDAO.add(user);
        gameDAO.add("testGame");
        authDAO.add(token, user);
    }

    @Test
    public void standardClear() throws DataAccessException {
        Assertions.assertEquals(userDAO.getUserByUsername("username"), user);
        Assertions.assertEquals(gameDAO.getById(1), game);
        Assertions.assertEquals(authDAO.getUserByToken(token), user);
    }
}