package service;

import dataaccess.*;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

class AuthorizationServiceTest {

    static AuthTokenDAO authTokenDAO;
    static UserDAO userDAO;

    static {
        try {
            var conn = DatabaseManager.getConnection();
            authTokenDAO = new SQLAuthTokenDAO(conn);
            userDAO = new SQLUserDAO(conn);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final UUID AUTH_TOKEN = UUID.randomUUID();

    @BeforeAll
    public static void setup() {
        userDAO.add(new UserModel("username", "password", "fake@email.com"));
        UserModel user = userDAO.getUserByUsername("username");
        authTokenDAO.add(AUTH_TOKEN, user);
    }

    @AfterAll
    public static void clearDB() {
        authTokenDAO.clearTokens();
        userDAO.clearTable();
    }

    @Test
    public void standardAuthorize() throws DataAccessException {
        Assertions.assertNotNull(AuthorizationService.authorize(AUTH_TOKEN.toString()));
    }


    @Test
    public void invalidAuthorize() {
        String invalidAuthToken = "asdf";

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            AuthorizationService.authorize(invalidAuthToken);
        });
    }

    @Test
    public void unauthorizedAuthorize() {
        String unauthorizedToken = UUID.randomUUID().toString();

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            AuthorizationService.authorize(unauthorizedToken);
        });
    }
}