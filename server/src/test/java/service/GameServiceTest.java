package service;

import chess.ChessGame;
import dataaccess.ChessGameDAO;
import dataaccess.DataAccessException;
import dataaccess.memorydao.MemoryChessGameDAO;
import models.ChessGameModel;
import org.junit.jupiter.api.*;

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
    void listGames() {
        dao.add("existing");

        ChessGameModel[] expectedGameList = { existingGame };
        assertArrayEquals(service.listGames().toArray(), expectedGameList);
    }

    @Test
    void joinGame() throws DataAccessException {
        dao.add("existing");

        service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK);
        service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE);

        ChessGameModel game = dao.getById(1);

        assertEquals("blackPlayer", game.getBlackUsername());
        assertEquals("whitePlayer", game.getWhiteUsername());

        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK));
        assertThrows(ValidationException.class,
                () -> service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE));
    }
}