package dataaccess.memorydao;

import dataaccess.UserDAO;
import models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private static final Map<String, UserModel> usersTable = new HashMap<>();

    @Override
    public void saveUser(UserModel user) {
        usersTable.put(user.username(), user);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return usersTable.getOrDefault(username, null);
    }

    public void clearTable() {
        usersTable.clear();
    }
}
