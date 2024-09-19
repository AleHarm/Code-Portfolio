package Handlers;

import Services.ClearService;
import Services.Responses.ClearResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class ClearHandler {

    public String clear(){

        Gson gson = new Gson();

        ClearResponse response = ClearService.clear();

        return gson.toJson(response);
    }
}
