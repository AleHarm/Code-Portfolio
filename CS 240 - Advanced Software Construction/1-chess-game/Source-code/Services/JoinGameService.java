package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import Models.Authtoken;
import Models.Game;
import Requests.JoinGameRequest;
import Responses.Response;

public class JoinGameService {

    public static Response joinGame(JoinGameRequest request, Authtoken authToken){

        Response joinGameResponse = new Response();
        Game game;

        //Make sure authToken exists in database
        try {
            authToken = AuthDAO.getAuthtoken(authToken);
        } catch (DataAccessException e) {
            joinGameResponse.setSuccess(false);
//            joinGameResponse.setMessage("Error: Authtoken could not be found in database");
            joinGameResponse.setMessage(e.getMessage());

            return joinGameResponse;
        }

        //Find game in database
        try{
            game = GameDAO.find(request.getGameID());
        } catch (DataAccessException e) {
            joinGameResponse.setSuccess(false);
            joinGameResponse.setMessage(e.getMessage());
            return joinGameResponse;
        }

        String username = authToken.getUsername();

        if(request.getPlayerColor() != null){

            String color = request.getPlayerColor().toLowerCase();

            if(color.equals("white")){

                if(game.getWhiteUsername() == null){
                    game.setWhiteUsername(username);
                }else{
                    joinGameResponse.setSuccess(false);
                    joinGameResponse.setMessage("Error: Spot already taken");
                    return joinGameResponse;
                }
            }else if(color.equals("black")){

                if(game.getBlackUsername() == null){
                    game.setBlackUsername(username);
                }else{
                    joinGameResponse.setSuccess(false);
                    joinGameResponse.setMessage("Error: Spot already taken");
                    return joinGameResponse;
                }
            }else if(color.equals("-white")){

                game.setWhiteUsername(null);
            }else if(color.equals("-black")){

                game.setBlackUsername(null);
            }
        }

        //Update game in database
        try {
            GameDAO.updateGame(game.getGameID(), game);
        } catch (DataAccessException e) {
            joinGameResponse.setSuccess(false);
//            joinGameResponse.setMessage("Error: Game could not be found in database");
            joinGameResponse.setMessage(e.getMessage());

            return joinGameResponse;
        }

        //Return info
        joinGameResponse.setSuccess(true);
        return joinGameResponse;
    }
}
