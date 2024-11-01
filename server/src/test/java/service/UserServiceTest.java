package service;

import dataaccess.*;
import models.UserModel;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.RegisterRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    public static UserService service;
    public static UserDAO dao;
    public static AuthTokenDAO authDao;

    static {
        try {
            var conn = DatabaseManager.getConnection();
            dao = new SQLUserDAO(conn);
            authDao = new SQLAuthTokenDAO(conn);
            service = new UserService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static UserModel user = new UserModel("username", "password", "fake@email.com");



    @BeforeEach
    void setup() {
        authDao.clearTokens();
        dao.clearTable();
    }

    @AfterAll
    static void clear() throws DataAccessException {
        new DBService().clearDB();
    }

    @Test
    void registerUser() {
        RegisterRequest request = new RegisterRequest("username", "password", "fake@email.com");
        service.registerUser(request);

        assertNotNull(dao.getUserByUsername("username"));
    }

    @Test
    void invalidRegister() {
        dao.add(user);
        RegisterRequest request = new RegisterRequest("username", "password", "fake@email.com");

        assertThrows(ValidationException.class, () -> service.registerUser(request));
    }

    @Test
    void successLoginUser() {
        registerTestUser();

        LoginRequest validRequest = new LoginRequest("username", "password");

        assertNotNull(service.loginUser(validRequest));
    }

    @Test
    void failLoginUser() {
        LoginRequest invalidRequest = new LoginRequest("username", "wrong");

        assertThrows(ValidationException.class, () -> service.loginUser(invalidRequest));
    }

    @Test
    void standardLogout() {
        registerTestUser();

        LoginRequest validRequest = new LoginRequest("username", "password");
        UUID token = service.loginUser(validRequest);


        assertDoesNotThrow(() -> service.logoutUser(token));
    }

    @Test
    void logoutWithWrongToken() {
        UUID wrongToken = UUID.randomUUID();
        assertDoesNotThrow(() -> service.logoutUser(wrongToken));
    }

    private void registerTestUser() {
        RegisterRequest request = new RegisterRequest("username", "password", "fake@email.com");
        service.registerUser(request);
    }
}