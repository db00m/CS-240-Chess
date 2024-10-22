package dataaccess.memorydao;

import dataaccess.UserDAO;
import models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private static final Map<String, UserModel> USERS_TABLE = new HashMap<>();

    @Override
    public void add(UserModel user) {
        USERS_TABLE.put(user.username(), user);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return USERS_TABLE.getOrDefault(username, null);
    }

    public void clearTable() {
        USERS_TABLE.clear();
    }
}
