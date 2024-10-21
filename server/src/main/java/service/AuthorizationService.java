package service;

import dataaccess.memorydao.MemoryAuthTokenDAO;
import models.UserModel;

import java.util.UUID;

public class AuthorizationService {
    public static UserModel authorize(String authTokenString) throws  UnauthorizedException {
        int standardTokenLength = UUID.randomUUID().toString().length();
        if (authTokenString == null ||
                authTokenString.length() > standardTokenLength ||
                authTokenString.length() < standardTokenLength) {
            throw new UnauthorizedException("Auth Token is not a valid UUID");
        }
        UserModel authorizedUser = new MemoryAuthTokenDAO().getUserByToken(UUID.fromString(authTokenString));

        if (authorizedUser == null) {
            throw new UnauthorizedException("User is not authorized");
        }

        return authorizedUser;
    }
}
