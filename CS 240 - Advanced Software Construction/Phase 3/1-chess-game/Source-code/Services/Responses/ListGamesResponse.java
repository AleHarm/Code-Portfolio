package Services.Responses;

import Models.Game;
import java.util.ArrayList;


public class ListGamesResponse extends Response{

    ArrayList<Game> games;

    public ListGamesResponse() {
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {

        this.games = games;
    }
}
