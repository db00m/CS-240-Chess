package dataaccess;

import models.UserModel;

public interface UserDAO {
    void add(UserModel user);
    UserModel getUserByUsername(String username);
    void clearTable();
}
