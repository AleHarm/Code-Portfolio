package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Responses.Response;

public class ClearService {

    public static Response clear(){

        Response clearResponse = new Response();

        //Clear database
        try {
            AuthDAO.clear();
            GameDAO.clear();
            UserDAO.clear();
        } catch (DataAccessException e) {
            clearResponse.setMessage("Error: Somehow the database couldn't be cleared");
            clearResponse.setSuccess(false);
            return clearResponse;
        }

        //Return info
        clearResponse.setSuccess(true);
        return clearResponse;
    }
}
