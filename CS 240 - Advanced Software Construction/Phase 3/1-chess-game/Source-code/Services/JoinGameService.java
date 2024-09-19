package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import Models.Authtoken;
import Models.Game;
import Services.Requests.JoinGameRequest;
import Services.Responses.JoinGameResponse;

public class JoinGameService {

    public static JoinGameResponse joinGame(JoinGameRequest request, Authtoken authToken){

        JoinGameResponse joinGameResponse = new JoinGameResponse();
        Game game;

        //Make sure authToken exists in database
        Authtoken dbAuthtoken = AuthDAO.getAuthtoken(authToken);
        if(dbAuthtoken == null) {
            joinGameResponse.setSuccess(false);
            joinGameResponse.setMessage("Error: Authtoken could not be found in database");
            return joinGameResponse;
        }

        //Find game in database
        try{
            game = GameDAO.find(request.getGameID());
        } catch (DataAccessException e) {
            joinGameResponse.setSuccess(false);
            joinGameResponse.setMessage("Error: Game could not be found in database");
            return joinGameResponse;
        }

        authToken = AuthDAO.getAuthtoken(authToken);
        String username = authToken.getUsername();

        if(request.getPlayerColor() != null){

            if(request.getPlayerColor().equals("WHITE")){

                if(game.getWhiteUsername() == null){
                    game.setWhiteUsername(username);
                }else{
                    joinGameResponse.setSuccess(false);
                    joinGameResponse.setMessage("Error: Spot already taken");
                    return joinGameResponse;
                }
            }else{

                if(game.getBlackUsername() == null){
                    game.setBlackUsername(username);
                }else{
                    joinGameResponse.setSuccess(false);
                    joinGameResponse.setMessage("Error: Spot already taken");
                    return joinGameResponse;
                }
            }
        }

        //Update game in database
        try {
            GameDAO.updateGame(game.getGameID(), game);
        } catch (DataAccessException e) {
            joinGameResponse.setSuccess(false);
            joinGameResponse.setMessage("Error: Game could not be found in database");
            return joinGameResponse;
        }

        //Return info
        joinGameResponse.setSuccess(true);
        return joinGameResponse;
    }
}
