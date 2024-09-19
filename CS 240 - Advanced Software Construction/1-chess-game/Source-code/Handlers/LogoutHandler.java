package Handlers;

import Models.Authtoken;
import Services.LogoutService;
import Responses.Response;
import com.google.gson.Gson;
import spark.Request;
import java.util.Objects;

public class LogoutHandler {
    public String handleRequest(Request req, spark.Response res){

        Gson gson = new Gson();
        Authtoken authToken = new Authtoken(req.headers("authorization"), null);

        Response response = LogoutService.logout(authToken);
        if(Objects.equals(response.getMessage(), "Error: Authtoken could not be found in database")){
            res.status(401);
        }

        return gson.toJson(response);
    }
}
