package dataaccess;

import models.UserModel;

import java.util.UUID;

public interface AuthTokenDAO {
    void add(UUID token, UserModel user);
    UserModel getUserByToken(UUID authToken);
    void delete(UUID authToken);
    void clearTokens();
}
