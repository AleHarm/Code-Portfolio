package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import Models.Authtoken;
import Responses.ListGamesResponse;

public class ListGamesService {

    public static ListGamesResponse listGames(Authtoken authToken){

        ListGamesResponse listGamesResponse = new ListGamesResponse();

        try {
            AuthDAO.getAuthtoken(authToken);
        } catch (DataAccessException e) {
            listGamesResponse.setSuccess(false);
            //listGamesResponse.setMessage("Error: Authtoken could not be found in database");
            listGamesResponse.setMessage(e.getMessage());

            return listGamesResponse;
        }

        //Return info
        try {
            listGamesResponse.setGames(GameDAO.findAll());
        } catch (DataAccessException e) {
            listGamesResponse.setSuccess(false);
            listGamesResponse.setMessage(e.getMessage());
            return listGamesResponse;
        }
        listGamesResponse.setSuccess(true);
        return listGamesResponse;
    }

}
