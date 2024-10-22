package service;

import dataaccess.UserDAO;
import dataaccess.memorydao.MemoryUserDAO;
import models.UserModel;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.RegisterRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    public static UserService service = new UserService();
    public static UserDAO dao = new MemoryUserDAO();

    public static UserModel user = new UserModel("username", "password", "fake@email.com");

    @BeforeEach
    void setup() {
        dao.clearTable();
    }

    @Test
    void registerUser() {
        RegisterRequest request = new RegisterRequest("username", "password", "fake@email.com");
        service.registerUser(request);

        assertEquals(user, dao.getUserByUsername("username"));
        assertThrows(ValidationException.class, () -> service.registerUser(request));
    }

    @Test
    void loginUser() {
        dao.add(user);

        LoginRequest validRequest = new LoginRequest("username", "password");
        LoginRequest invalidRequest = new LoginRequest("username", "wrong");

        assertNotNull(service.loginUser(validRequest));
        assertThrows(ValidationException.class, () -> service.loginUser(invalidRequest));
    }

    @Test
    void logoutUser() {
        dao.add(user);

        LoginRequest validRequest = new LoginRequest("username", "password");
        UUID token = service.loginUser(validRequest);
        UUID wrongToken = UUID.randomUUID();

        assertDoesNotThrow(() -> service.logoutUser(token));
        assertThrows(UnauthorizedException.class, () -> service.logoutUser(wrongToken));
    }
}