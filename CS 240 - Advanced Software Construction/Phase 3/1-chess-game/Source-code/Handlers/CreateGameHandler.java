package Handlers;

import Models.Authtoken;
import Services.CreateGameService;
import Services.Requests.CreateGameRequest;
import Services.Responses.CreateGameResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class CreateGameHandler {

    public String handleRequest(Request req, Response res){

        Gson gson = new Gson();

        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        Authtoken authToken = new Authtoken(req.headers("authorization"), null);

        CreateGameResponse response = CreateGameService.createGame(request, authToken);
        if(Objects.equals(response.getMessage(), "Error: Authtoken could not be found in database")){

            res.status(401);
        }

        return gson.toJson(response);
    }
}
