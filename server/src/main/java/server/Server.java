package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handlers.*;
import spark.*;
import websocket.WSServer;

public class Server {

    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException exc) {
            throw new RuntimeException(exc);
        }

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WSServer.class);

        Spark.delete("/db", new ClearDBHandler());

        Spark.post("/user", new RegisterHandler());

        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());

        Spark.get("/game", new ListGamesHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());



        //This line initializes the server and can be removed once you have a functioning endpoint 

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
