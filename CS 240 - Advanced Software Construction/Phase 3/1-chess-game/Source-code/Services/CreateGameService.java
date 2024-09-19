package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import Models.Authtoken;
import Models.Game;
import Services.Requests.CreateGameRequest;
import Services.Responses.CreateGameResponse;

public class CreateGameService {

    public static CreateGameResponse createGame(CreateGameRequest request, Authtoken authToken){

        CreateGameResponse createGameResponse = new CreateGameResponse();

        //Make sure authToken exists in database
        Authtoken dbAuthtoken = AuthDAO.getAuthtoken(authToken);
        if(dbAuthtoken == null) {
            createGameResponse.setSuccess(false);
            createGameResponse.setMessage("Error: Authtoken could not be found in database");
            return createGameResponse;
        }

        //Make game
        int gameID = createGameID();
        Game game = new Game(gameID, request.getGameName());

        //Try to add game to database
        try{
            GameDAO.insert(game);
        } catch (DataAccessException e) {
            createGameResponse.setSuccess(false);
            createGameResponse.setMessage("Error: Game could not be created in database");
            return createGameResponse;
        }

        //Return info
        createGameResponse.setSuccess(true);
        createGameResponse.setGameID(gameID);
        return createGameResponse;
    }

    private static int createGameID(){

        return GameDAO.findAll().size() + 1;
    }
}
