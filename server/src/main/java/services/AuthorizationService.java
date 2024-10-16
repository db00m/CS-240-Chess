package services;

import dataaccess.memorydao.MemoryAuthTokenDAO;
import models.UserModel;

import java.util.UUID;

public class AuthorizationService {
    public static void authorize(UUID authToken) throws  UnauthorizedException {
        UserModel authorizedUser = new MemoryAuthTokenDAO().getUserByToken(authToken);
        if (authorizedUser == null) {
            throw new UnauthorizedException("User is not authorized");
        }
    }
}
