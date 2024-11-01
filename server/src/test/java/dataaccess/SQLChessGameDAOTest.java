package dataaccess;

import models.ChessGameModel;
import models.UserModel;
import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SQLChessGameDAOTest {

    static Connection conn;
    static ChessGameDAO gameDAO;
    static UserDAO userDAO;

    static {
        try {
            conn = DatabaseManager.getConnection();
            gameDAO = new SQLChessGameDAO(conn);
            userDAO = new SQLUserDAO(conn);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final static String GAME_NAME = "newGame";
    private final static UserModel FAKE_USER = new UserModel(0, "fake", "fake", "fake");
    private final static ChessGameModel FAKE_GAME = new ChessGameModel(0, "Fake");

    @BeforeAll
    static void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        gameDAO.clear();
        userDAO.clearTable();
    }

    @AfterEach
    void clearDB() {
        gameDAO.clear();
        userDAO.clearTable();
    }

    @Test
    void addWithGameName() {
        assertDoesNotThrow(() -> gameDAO.add(GAME_NAME));

        assertCorrectColumnsArePopulated();
    }

    @Test
    void addWithoutGameName() {
        assertThrows(RuntimeException.class, () -> gameDAO.add(null));
    }

    @Test
    void getByIdWhenGameExists() throws DataAccessException {
        int id = addGame();

        assertEquals(getExistingGame(), gameDAO.getById(id));
    }

    @Test
    void getByIdWhenGameDoesNotExist() {
        assertThrows(DataAccessException.class, () -> gameDAO.getById(1));
    }

    @Test
    void setBlackUserWhenUserExists() {
        UserModel black = addAndGetUser("black");
        assertDoesNotThrow(() -> gameDAO.setBlackUser(addAndGetGame(), black));
    }

    @Test
    void setBlackUserWhenUserDoesNotExist() {
        assertThrows(RuntimeException.class, () -> gameDAO.setBlackUser(addAndGetGame(), FAKE_USER));
    }

    @Test
    void setWhiteUserWhenGameExists()  {
        UserModel white = addAndGetUser("white");
        assertDoesNotThrow(() -> gameDAO.setBlackUser(addAndGetGame(), white));
    }

    @Test
    void setWhiteUserWhenGameDoesNotExist() {
        UserModel white = addAndGetUser("white");
        assertDoesNotThrow(() -> gameDAO.setWhiteUser(FAKE_GAME, white));
    }

    @Test
    void getAllWhenGameExists() throws DataAccessException {
        addGame();
        ChessGameModel[] expectedGames = { getExistingGame() };

        assertArrayEquals(expectedGames, gameDAO.getAll().toArray());
    }

    @Test
    void getAllWhenGameDoesNotExist() {
        assertArrayEquals(Collections.emptyList().toArray(), gameDAO.getAll().toArray());
    }

    @Test
    void clearWithPopulatedTable() throws DataAccessException {
        addGame();
        assertDoesNotThrow(() -> gameDAO.clear());
    }

    @Test
    void clearWithEmptyTable() {
        assertDoesNotThrow(() -> gameDAO.clear());
    }

    private void assertCorrectColumnsArePopulated() {
        var statement = """
                SELECT name, game_data FROM chess_games LIMIT 1
                """;
        try (var preparedStatement = conn.prepareStatement(statement)) {
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    assertNotNull(rs.getString("name"), "name column is not populated");
                    assertNotNull(rs.getString("game_data"), "game_data column is not populated");
                } else {
                    throw new AssertionFailedError("No rows were populated in chess_games table");
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int addGame() throws DataAccessException {
        return gameDAO.add(GAME_NAME);
    }

    private ChessGameModel getExistingGame() {
        var statement = """
                SELECT
                    chess_games.id, name, white.username AS white_username, black.username AS black_username
                FROM
                    chess_games
                LEFT JOIN
                    users AS white ON chess_games.white_user_id = white.id
                LEFT JOIN
                    users AS black ON chess_games.black_user_id = black.id
                LIMIT 1
                """;
        try(var preparedStatement = conn.prepareStatement(statement)) {
            ChessGameModel game = ChessGameDAO.getChessGame(preparedStatement);
            if (game != null) {
                return game;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private ChessGameModel addAndGetGame() throws DataAccessException {
        addGame();
        return getExistingGame();
    }

    private UserModel addAndGetUser(String username) {
        userDAO.add(new UserModel(username, username, username));
        return userDAO.getUserByUsername(username);
    }
}