package service;

import chess.ChessGame;
import dataaccess.*;
import models.ChessGameModel;
import models.UserModel;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    public static ChessGameDAO dao = new MemoryChessGameDAO();
    public static UserDAO userDAO = new MemoryUserDAO();
    public static GameService service;

    static {
        try {
            service = new GameService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChessGameModel existingGame = new ChessGameModel(1, "existing");
    public static ChessGameModel newGame = new ChessGameModel(2, "new");

    @BeforeEach
    void setup() {
        dao.clear();
    }

    @Test
    void createGame() throws DataAccessException {
        dao.add("existing");

        assertEquals(service.createGame("new"), 2);
    }

    @Test
    void duplicateCreate() throws DataAccessException {
        dao.add("existing");

        assertEquals(service.createGame("existing"), 2);

    }

    @Test
    void listGames() throws DataAccessException {
        dao.add("existing");

        ChessGameModel[] expectedGameList = { existingGame };
        assertArrayEquals(service.listGames().toArray(), expectedGameList);
    }

    @Test
    void emptyListGames() {
        assertArrayEquals(service.listGames().toArray(), Collections.emptyList().toArray());
    }

    @Test
    void joinGame() throws DataAccessException {
        dao.add("existing");
        userDAO.add(new UserModel("blackPlayer", "password", "email"));
        userDAO.add(new UserModel("whitePlayer", "password", "email"));

        service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK);
        service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE);

        ChessGameModel game = dao.getById(1);

        assertEquals("blackPlayer", game.getBlackUsername());
        assertEquals("whitePlayer", game.getWhiteUsername());
    }

    @Test
    void failJoinGame() throws DataAccessException {
        dao.add("existing");
        userDAO.add(new UserModel("blackPlayer", "password", "email"));
        userDAO.add(new UserModel("whitePlayer", "password", "email"));
        dao.getById(1).setBlackUsername("blackPlayer");
        dao.getById(1).setWhiteUsername("whitePlayer");

        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK));
        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE));
    }
}