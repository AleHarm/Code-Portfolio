package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import Models.Authtoken;
import Services.Responses.LogoutResponse;

public class LogoutService {

    public static LogoutResponse logout(Authtoken authToken){
        LogoutResponse response = new LogoutResponse();

        //Make sure authToken exists in database
        Authtoken dbAuthtoken = AuthDAO.getAuthtoken(authToken);
        if(dbAuthtoken == null) {
            response.setSuccess(false);
            response.setMessage("Error: Authtoken could not be found in database");
            return response;
        }

        //Delete authToken from list
        try{
            AuthDAO.deleteAuthToken(authToken);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            response.setMessage("Error: Authtoken could not be deleted from database");
            return response;
        }

        //Return info
        response.setSuccess(true);
        return response;
    }
}
