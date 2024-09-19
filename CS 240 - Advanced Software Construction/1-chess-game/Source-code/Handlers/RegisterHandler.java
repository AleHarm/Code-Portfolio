package Handlers;

import Services.RegisterService;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class RegisterHandler {

    public String handleRequest(Request req, Response res){

        Gson gson = new Gson();

        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

        LoginResponse response = RegisterService.register(request);



        if(Objects.equals(response.getMessage(), "Error: Bad request, no password")){
            res.status(400);
        }

        if(Objects.equals(response.getMessage(), "Error: User could not be created in database")){
            res.status(403);
        }

        return gson.toJson(response);
    }
}
