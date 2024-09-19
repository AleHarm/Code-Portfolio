package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import Models.Authtoken;
import Responses.Response;

public class LogoutService {

    public static Response logout(Authtoken authToken){
        Response response = new Response();

        //Make sure authToken exists in database
        try {
            authToken = AuthDAO.getAuthtoken(authToken);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            //response.setMessage("Error: Authtoken could not be found in database");
            response.setMessage(e.getMessage());

            return response;
        }

        //Delete authToken from list
        try{
            AuthDAO.deleteAuthToken(authToken);
        } catch (DataAccessException e) {
            response.setSuccess(false);
//            response.setMessage("Error: Authtoken could not be deleted from database");
            response.setMessage(e.getMessage());

            return response;
        }

        //Return info
        response.setSuccess(true);
        return response;
    }
}
