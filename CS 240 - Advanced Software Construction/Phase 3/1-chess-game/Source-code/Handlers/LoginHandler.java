package Handlers;

import Services.LoginService;
import Services.Requests.LoginRequest;
import Services.Responses.LoginResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler {

    public String handleRequest(Request req, Response res){

        Gson gson = new Gson();

        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

        LoginResponse response = LoginService.login(request);
        if(Objects.equals(response.getMessage(), "Error: User could not be found in database")){
            res.status(401);
        }

        if(Objects.equals(response.getMessage(), "Error: Incorrect password")){
            res.status(401);
        }

        return gson.toJson(response);
    }
}
