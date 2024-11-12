package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import service.DBService;
import service.UserService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static DBService dbService = new DBService();


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        facade = new ServerFacade("http://localhost:" + String.valueOf(port));

        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearDB() throws DataAccessException {
        dbService.clearDB();
    }

    @AfterAll
    static void stopServer() throws DataAccessException {
        dbService.clearDB();
        server.stop();
    }


    @Test
    public void standardRegister() throws IOException {
        registerUser();
    }

    @Test
    public void registerWithError() {
        assertThrows(IOException.class, () -> facade.register(null, "u", "u"));
    }

    @Test
    public void standardLogin() throws IOException {
        registerUser();
        assertDoesNotThrow(() -> facade.login("u", "u"));
    }

    @Test
    public void loginWithError() throws IOException {
        registerUser();
        assertThrows(IOException.class, () -> facade.login("u", "q"));
    }

    @Test
    public void standardLogout() throws IOException {
        String token = registerUser();
        assertDoesNotThrow(() -> facade.logout(token));
    }

    @Test
    public void logoutWithError() throws IOException {
        registerUser();
        assertThrows(IOException.class, () -> facade.logout("fake token"));
    }

    @Test
    public void standardCreate() throws  IOException {
        String token = registerUser();
        assertDoesNotThrow(() -> facade.createGame(token, "New Game"));
    }

    @Test
    public void createWithError() throws  IOException {
        registerUser();
        assertThrows(IOException.class, () -> facade.createGame("fake token", "New Game"));
    }

    @Test
    public void standardList() throws IOException {
        String token = registerUser();
        assertDoesNotThrow(() -> facade.getGameList(token));
    }

    @Test
    public void listWithError() throws IOException {
        registerUser();
        assertThrows(IOException.class, () -> facade.getGameList("fake"));
    }

    private String registerUser() throws IOException {
        return facade.register("u", "u", "u");
    }
}
