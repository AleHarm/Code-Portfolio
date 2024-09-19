package Handlers;

import Services.ClearService;
import Responses.Response;
import com.google.gson.Gson;

public class ClearHandler {

    public String clear(){

        Gson gson = new Gson();

        Response response = ClearService.clear();

        return gson.toJson(response);
    }
}
