package dataaccess;

import models.UserModel;

public interface UserDAO {
    void saveUser(UserModel user);
    UserModel getUserByUsername(String username);
    void clearTable();
}
