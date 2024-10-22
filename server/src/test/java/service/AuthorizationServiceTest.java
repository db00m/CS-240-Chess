package service;

import dataaccess.AuthTokenDAO;
import dataaccess.memorydao.MemoryAuthTokenDAO;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

class AuthorizationServiceTest {

    private static final AuthTokenDAO DAO = new MemoryAuthTokenDAO();
    private static final UserModel LOGGED_IN_USER = new UserModel("username", "password", "fake@email.com");
    private static final UUID AUTH_TOKEN = UUID.randomUUID();

    @BeforeAll
    public static void setup() {
        DAO.add(AUTH_TOKEN, LOGGED_IN_USER);
    }

    @Test
    public void standardAuthorize() {
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