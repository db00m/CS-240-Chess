package server;

import handlers.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", new ClearDBHandler());

        Spark.post("/user", new RegisterHandler());

        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());

        Spark.get("/game", new ListGamesHandler());


        //This line initializes the server and can be removed once you have a functioning endpoint 

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
