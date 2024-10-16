package services;

import dataaccess.memorydao.MemoryAuthTokenDAO;
import dataaccess.memorydao.MemoryUserDAO;

public class DBService {
    public void clearDB() {
        new MemoryUserDAO().clearTable();
        new MemoryAuthTokenDAO().clearTokens();
        // new MemoryChessGameDOA().clear();
    }
}
