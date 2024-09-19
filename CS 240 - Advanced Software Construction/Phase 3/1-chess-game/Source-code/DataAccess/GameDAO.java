package DataAccess;

import DatabaseTEMP.DatabaseTEMP;
import Models.Game;

import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    private static final DatabaseTEMP database = DatabaseTEMP.getDatabase();

    public static void insert(Game game) throws DataAccessException{

        try{
            find(game.getGameID());

        } catch (DataAccessException e) {
            database.games.add(game);
            return;
        }

        throw new DataAccessException();
    }

    public static Game find(int gameID) throws DataAccessException{

         for(int i = 0; i < database.games.size(); i++){

             Game currentGame = database.games.get(i);

             if(currentGame.getGameID() == gameID){

                 return currentGame;
             }
         }

         throw new DataAccessException();
    }

    public static ArrayList<Game> findAll(){

        return database.games;
    }

    public static void updateGame(int gameID, Game game) throws DataAccessException{

        for(int i = 0; i < database.games.size(); i++){

            Game currentGame = database.games.get(i);

            if(currentGame.getGameID() == gameID){

                database.games.set(i, game);
                return;
            }
        }

        throw new DataAccessException();
    }
}
