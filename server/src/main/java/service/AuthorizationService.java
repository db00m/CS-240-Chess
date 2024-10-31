package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import models.UserModel;

import java.util.UUID;

public class AuthorizationService {
    public static UserModel authorize(String authTokenString) throws UnauthorizedException, DataAccessException {
        int standardTokenLength = UUID.randomUUID().toString().length();
        if (authTokenString == null ||
                authTokenString.length() > standardTokenLength ||
                authTokenString.length() < standardTokenLength) {
            throw new UnauthorizedException("Auth Token is not a valid UUID");
        }
        UserModel authorizedUser = new SQLAuthTokenDAO().getUserByToken(UUID.fromString(authTokenString));

        if (authorizedUser == null) {
            throw new UnauthorizedException("User is not authorized");
        }

        return authorizedUser;
    }
}
