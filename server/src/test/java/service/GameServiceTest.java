package service;

import chess.ChessGame;
import dataaccess.*;
import models.ChessGameModel;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    static ChessGameDAO gameDAO;
    static UserDAO userDAO;
    static GameService service;

    static {
        try {
            service = new GameService();

            var conn = DatabaseManager.getConnection();
            gameDAO = new SQLChessGameDAO(conn);
            userDAO = new SQLUserDAO(conn);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChessGameModel existingGame = new ChessGameModel(1, "existing");
    public static ChessGameModel newGame = new ChessGameModel(2, "new");

    @BeforeEach
    void setup() {
        gameDAO.clear();
    }

    @Test
    void createGame() throws DataAccessException {
        gameDAO.add("existing");
        int id = service.createGame("new");

        assertEquals(gameDAO.getById(id).getGameName(), newGame.getGameName());
    }

    @Test
    void duplicateCreate() throws DataAccessException {
        gameDAO.add("existing");
        int id = service.createGame("new");

        assertEquals(gameDAO.getById(id).getGameName(), newGame.getGameName());
    }

    @Test
    void listGames() throws DataAccessException {
        int id = gameDAO.add("existing");

        ChessGameModel[] expectedGameList = { gameDAO.getById(id) };
        assertArrayEquals(service.listGames().toArray(), expectedGameList);
    }

    @Test
    void emptyListGames() {
        assertArrayEquals(service.listGames().toArray(), Collections.emptyList().toArray());
    }

    @Test
    void joinGame() throws DataAccessException {
        int id = gameDAO.add("existing");
        userDAO.add(new UserModel("blackPlayer", "password", "email"));
        userDAO.add(new UserModel("whitePlayer", "password", "email"));

        service.joinGame(id, "blackPlayer", ChessGame.TeamColor.BLACK);
        service.joinGame(id, "whitePlayer", ChessGame.TeamColor.WHITE);

        ChessGameModel game = gameDAO.getById(id);

        assertEquals("blackPlayer", game.getBlackUsername());
        assertEquals("whitePlayer", game.getWhiteUsername());
    }

    @Test
    void failJoinGame() throws DataAccessException {
        int id = gameDAO.add("existing");
        userDAO.add(new UserModel("blackPlayer", "password", "email"));
        userDAO.add(new UserModel("whitePlayer", "password", "email"));
        ChessGameModel game = gameDAO.getById(id);
        gameDAO.setBlackUser(game, userDAO.getUserByUsername("blackPlayer"));
        gameDAO.setWhiteUser(game, userDAO.getUserByUsername("whitePlayer"));

        assertThrows(ValidationException.class,
                () -> service.joinGame(id, "blackPlayer", ChessGame.TeamColor.BLACK));
        assertThrows(ValidationException.class,
                () -> service.joinGame(id, "whitePlayer", ChessGame.TeamColor.WHITE));
    }
}