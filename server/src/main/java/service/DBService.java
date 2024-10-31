package service;

import dataaccess.*;

public class DBService {
    public void clearDB() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        new SQLAuthTokenDAO(conn).clearTokens();
        new SQLChessGameDAO(conn).clear();
        new SQLUserDAO(conn).clearTable();
    }
}
