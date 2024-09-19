package Server;

import Handlers.*;
import spark.Spark;

public class Server {

    public Server(){};

    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

            Spark.externalStaticFileLocation("web");

            createRoutes();

            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    private static void createRoutes() {
        Spark.post("/user", (req, res) -> {
            return new RegisterHandler().handleRequest(req, res);
        });
        Spark.post("/session", (req, res) -> {
            return new LoginHandler().handleRequest(req, res);
        });
        Spark.delete("/session", (req, res)-> {
            return new LogoutHandler().handleRequest(req, res);
        });
        Spark.delete("/db", (req, res) ->{
            return new ClearHandler().clear();
        });
        Spark.get("/game", (req, res) ->{
            return new ListGamesHandler().handleRequest(req, res);
        });
        Spark.post("/game", (req, res) ->{
           return new CreateGameHandler().handleRequest(req, res);
        });
        Spark.put("/game", (req, res) -> {
            return new JoinGameHandler().handleRequest(req, res);
        });
    }
}
