package Services;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Models.Authtoken;
import Services.Responses.ListGamesResponse;

public class ListGamesService {

    public static ListGamesResponse listGames(Authtoken authToken){

        ListGamesResponse listGamesResponse = new ListGamesResponse();

        Authtoken dbAuthtoken = AuthDAO.getAuthtoken(authToken);
        if(dbAuthtoken == null) {
            listGamesResponse.setSuccess(false);
            listGamesResponse.setMessage("Error: Authtoken could not be found in database");
            return listGamesResponse;
        }

        //Return info
        listGamesResponse.setGames(GameDAO.findAll());
        listGamesResponse.setSuccess(true);
        return listGamesResponse;
    }

}
