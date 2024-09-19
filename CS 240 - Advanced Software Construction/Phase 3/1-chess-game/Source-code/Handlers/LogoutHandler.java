package Handlers;

import Models.Authtoken;
import Services.LogoutService;
import Services.Responses.LogoutResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LogoutHandler {
    public String handleRequest(Request req, Response res){

        Gson gson = new Gson();
        Authtoken authToken = new Authtoken(req.headers("authorization"), null);

        LogoutResponse response = LogoutService.logout(authToken);
        if(Objects.equals(response.getMessage(), "Error: Authtoken could not be found in database")){
            res.status(401);
        }

        return gson.toJson(response);
    }
}
