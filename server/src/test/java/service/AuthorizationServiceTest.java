package service;

import dataaccess.AuthTokenDAO;
import dataaccess.memorydao.MemoryAuthTokenDAO;
import models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

class AuthorizationServiceTest {

    private static final AuthTokenDAO dao = new MemoryAuthTokenDAO();
    private static final UserModel loggedInUser = new UserModel("username", "password", "fake@email.com");
    private static final UUID authToken = UUID.randomUUID();

    @BeforeAll
    public static void setup() {
        dao.add(authToken, loggedInUser);
    }

    @Test
    public void standardAuthorization() {
        Assertions.assertNotNull(AuthorizationService.authorize(authToken.toString()));
    }

    @Test
    public void withInvalidAuthToken() {
        String invalidAuthToken = "asdf";
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            AuthorizationService.authorize(invalidAuthToken);
        });
    }

    @Test
    public void withUnauthorizedToken() {
        String unauthorizedToken = UUID.randomUUID().toString();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            AuthorizationService.authorize(unauthorizedToken);
        });
    }
}