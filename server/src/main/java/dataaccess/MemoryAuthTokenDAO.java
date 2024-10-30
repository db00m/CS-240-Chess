package dataaccess;

import models.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthTokenDAO implements AuthTokenDAO {

    private static final Map<UUID, UserModel> AUTH_TOKENS = new HashMap<>();

    @Override
    public void add(UUID token, UserModel user) {
        AUTH_TOKENS.put(token, user);
    }

    @Override
    public UserModel getUserByToken(UUID authToken) {
        return AUTH_TOKENS.get(authToken);
    }

    @Override
    public void delete(UUID authToken) {
        AUTH_TOKENS.remove(authToken);
    }

    @Override
    public void clearTokens() {
        AUTH_TOKENS.clear();
    }
}
