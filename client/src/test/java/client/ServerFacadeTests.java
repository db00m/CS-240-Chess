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
    public void standardRegister() {
        registerUser();
    }

    @Test
    public void registerWithError() {
        assertThrows(IOException.class, () -> facade.register(null, "u", "u"));
    }

    @Test
    public void standardLogin() {
        registerUser();
        assertDoesNotThrow(() -> facade.login("u", "u"));
    }

    @Test
    public void loginWithError() {
        registerUser();
        assertThrows(IOException.class, () -> facade.login("u", "q"));
    }

    @Test
    public void standardLogout() throws IOException {
        String token  = facade.register("u", "u", "u");
        assertDoesNotThrow(() -> facade.logout(token));
    }

    @Test void logoutWithError() {
        registerUser();
        assertThrows(IOException.class, () -> facade.logout("fake token"));
    }

    private void registerUser() {
        assertDoesNotThrow(() -> facade.register("u", "u", "u"));
    }
}
