package Handlers;

import Models.Authtoken;
import Services.JoinGameService;
import Requests.JoinGameRequest;
import Responses.Response;
import com.google.gson.Gson;
import spark.Request;
import java.util.Objects;

public class JoinGameHandler {

    public String handleRequest(Request req, spark.Response res){

        Gson gson = new Gson();

        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);

        Authtoken authToken = new Authtoken(req.headers("authorization"), null);

        Response response = JoinGameService.joinGame(request, authToken);
        if(Objects.equals(response.getMessage(), "Error: Authtoken could not be found in database")){

            res.status(401);
        }

        if(Objects.equals(response.getMessage(), "Error: Game could not be found in database")){

            res.status(400);
        }

        if(Objects.equals(response.getMessage(), "Error: Spot already taken")){

            res.status(403);
        }

        return gson.toJson(response);
    }
}
