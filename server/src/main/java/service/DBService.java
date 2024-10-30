package service;

import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryChessGameDAO;
import dataaccess.MemoryUserDAO;

public class DBService {
    public void clearDB() {
        new MemoryUserDAO().clearTable();
        new MemoryAuthTokenDAO().clearTokens();
        new MemoryChessGameDAO().clear();
    }
}
