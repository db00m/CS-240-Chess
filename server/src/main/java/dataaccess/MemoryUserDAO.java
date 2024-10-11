package dataaccess;

import models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private Map<String, UserModel> usersTable = new HashMap<>();

    @Override
    public void saveUser(UserModel user) {
        usersTable.put(user.username(), user);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return usersTable.get(username);
    }
    
    public void clearTable() {
        usersTable = new HashMap<>();
    }
}
