package service;

import dataaccess.*;
import models.UserModel;
import requests.LoginRequest;
import requests.RegisterRequest;

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthTokenDAO authTokenDAO = new SQLAuthTokenDAO();

    public UserService() throws DataAccessException {
    }

    public void registerUser(RegisterRequest request) throws ValidationException {
        UserModel existingUser = userDAO.getUserByUsername(request.username());

        if (existingUser == null) {
            userDAO.add(new UserModel(request.username(), request.password(), request.email()));
        } else {
            throw new ValidationException("Username already taken");
        }
    }

    public UUID loginUser(LoginRequest request) throws ValidationException {
        UserModel user =  userDAO.getUserByUsername(request.username());
        userDAO.validatePassword(user, request.password());

        UUID authToken = UUID.randomUUID();
        authTokenDAO.add(authToken, user);

        return authToken;
    }

    public void logoutUser(UUID authToken) throws UnauthorizedException {
        authTokenDAO.delete(authToken);
    }
}
