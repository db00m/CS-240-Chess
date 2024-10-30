package service;

import chess.ChessGame;
import dataaccess.ChessGameDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryChessGameDAO;
import models.ChessGameModel;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    public static ChessGameDAO dao = new MemoryChessGameDAO();
    public static GameService service = new GameService();

    public static ChessGameModel existingGame = new ChessGameModel(1, "existing");
    public static ChessGameModel newGame = new ChessGameModel(2, "new");

    @BeforeEach
    void setup() {
        dao.clear();
    }

    @Test
    void createGame() {
        dao.add("existing");

        assertEquals(service.createGame("new"), 2);
    }

    @Test
    void duplicateCreate() {
        dao.add("existing");

        assertEquals(service.createGame("existing"), 2);

    }

    @Test
    void listGames() {
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

        service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK);
        service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE);

        ChessGameModel game = dao.getById(1);

        assertEquals("blackPlayer", game.getBlackUsername());
        assertEquals("whitePlayer", game.getWhiteUsername());
    }

    @Test
    void failJoinGame() throws DataAccessException {
        dao.add("existing");
        dao.getById(1).setBlackUsername("blackPlayer");
        dao.getById(1).setWhiteUsername("whitePlayer");

        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK));
        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE));
    }
}