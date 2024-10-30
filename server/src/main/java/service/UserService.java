package service;

import dataaccess.AuthTokenDAO;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import models.UserModel;
import requests.LoginRequest;
import requests.RegisterRequest;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthTokenDAO authTokenDAO = new MemoryAuthTokenDAO();

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
