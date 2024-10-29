import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        DatabaseManager.createDatabase();
        new Server().run(8080);
    }
}