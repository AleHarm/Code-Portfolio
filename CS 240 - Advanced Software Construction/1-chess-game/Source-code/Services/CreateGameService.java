package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import Models.Authtoken;
import Models.Game;
import Requests.CreateGameRequest;
import Responses.CreateGameResponse;

public class CreateGameService {

    public static CreateGameResponse createGame(CreateGameRequest request, Authtoken authToken){

        CreateGameResponse createGameResponse = new CreateGameResponse();
        int gameID;
        //Make sure authToken exists in database
        try {
            AuthDAO.getAuthtoken(authToken);
        } catch (DataAccessException e) {
            createGameResponse.setSuccess(false);
            //createGameResponse.setMessage("Error: Authtoken could not be found in database");
            createGameResponse.setMessage(e.getMessage());

            return createGameResponse;
        }

        //Make game
        Game game = new Game(request.getGameName());

        //Try to add game to database
        try{
            gameID = GameDAO.insert(game);
        } catch (DataAccessException e) {
            createGameResponse.setSuccess(false);
            //createGameResponse.setMessage("Error: Game could not be created in database");
            createGameResponse.setMessage(e.getMessage());

            return createGameResponse;
        }

        //Return info
        createGameResponse.setGameID(gameID);
        createGameResponse.setSuccess(true);
        return createGameResponse;
    }
}
