package service;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
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
    }

    @Test
    void invalidRegister() {
        dao.add(user);
        RegisterRequest request = new RegisterRequest("username", "password", "fake@email.com");

        assertThrows(ValidationException.class, () -> service.registerUser(request));
    }

    @Test
    void successLoginUser() {
        dao.add(user);

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
        dao.add(user);

        LoginRequest validRequest = new LoginRequest("username", "password");
        UUID token = service.loginUser(validRequest);


        assertDoesNotThrow(() -> service.logoutUser(token));
    }

    @Test
    void logoutWithWrongToken() {
        UUID wrongToken = UUID.randomUUID();
        assertDoesNotThrow(() -> service.logoutUser(wrongToken));
    }
}