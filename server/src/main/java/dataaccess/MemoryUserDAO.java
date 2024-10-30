package dataaccess;

import models.UserModel;
import service.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private static final Map<String, UserModel> USERS_TABLE = new HashMap<>();

    @Override
    public void add(UserModel user) {
        USERS_TABLE.put(user.getUsername(), user);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return USERS_TABLE.getOrDefault(username, null);
    }

    public void validatePassword(UserModel user, String password) throws ValidationException {
        if (user == null || !Objects.equals(user.getPassword(), password)) {
            throw new ValidationException("Invalid login credentials");
        }
    }

    public void clearTable() {
        USERS_TABLE.clear();
    }
}
