package service;

import dataaccess.*;
import models.UserModel;
import org.mindrot.jbcrypt.BCrypt;
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
            userDAO.add(new UserModel(request.username(), encryptPassword(request.password()), request.email()));
        } else {
            throw new ValidationException("Username already taken");
        }
    }

    public UUID loginUser(LoginRequest request) throws ValidationException {
        UserModel user =  userDAO.getUserByUsername(request.username());
        validatePassword(user, request.password());

        UUID authToken = UUID.randomUUID();
        authTokenDAO.add(authToken, user);

        return authToken;
    }

    public void logoutUser(UUID authToken) throws UnauthorizedException {
        authTokenDAO.delete(authToken);
    }

    private void validatePassword(UserModel user, String password) {
        if (user == null || !BCrypt.checkpw(password, user.password())){
            throw new ValidationException("Invalid login credentials");
        }
    }

    private String encryptPassword(String unencryptedPassword) {
        return BCrypt.hashpw(unencryptedPassword, BCrypt.gensalt());
    }
}
