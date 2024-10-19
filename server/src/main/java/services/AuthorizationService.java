package services;

import dataaccess.memorydao.MemoryAuthTokenDAO;
import models.UserModel;

import java.util.UUID;

public class AuthorizationService {
    public static void authorize(String authTokenString) throws  UnauthorizedException {
        if (authTokenString == null) {
            throw new UnauthorizedException("Auth Token was not provided in request header");
        }

        UserModel authorizedUser = new MemoryAuthTokenDAO().getUserByToken(UUID.fromString(authTokenString));

        if (authorizedUser == null) {
            throw new UnauthorizedException("User is not authorized");
        }
    }
}
