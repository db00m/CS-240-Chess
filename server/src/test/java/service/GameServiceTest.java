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

    @BeforeAll
    static void setup() {
        dao.add("existing");
    }

    @Test
    void createGame() {
        Assertions.assertEquals(service.createGame("new"), 2);
    }

    @Test
    void listGames() {
        ChessGameModel[] expectedGameList = { existingGame };
        Assertions.assertArrayEquals(service.listGames().toArray(), expectedGameList);
    }

    @Test
    void joinGame() throws DataAccessException {
        service.joinGame(1, "blackPlayer", ChessGame.TeamColor.BLACK);
        service.joinGame(1, "whitePlayer", ChessGame.TeamColor.WHITE);

        ChessGameModel game = dao.getById(1);

        Assertions.assertEquals("blackPlayer", game.getBlackUsername());
        Assertions.assertEquals("whitePlayer", game.getWhiteUsername());
    }
}