package dataaccess.memorydao;

import dataaccess.AuthTokenDAO;
import models.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthTokenDAO implements AuthTokenDAO {

    private static final Map<UUID, UserModel> authTokens = new HashMap<>();

    @Override
    public void add(UUID token, UserModel user) {
        authTokens.put(token, user);
    }

    @Override
    public UserModel getUserByToken(UUID authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void delete(UUID authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public void clearTokens() {
        authTokens.clear();
    }
}
