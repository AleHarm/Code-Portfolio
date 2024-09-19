package DatabaseTEMP;

import Models.Authtoken;
import Models.Game;
import Models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTEMP {

    private static DatabaseTEMP database;

    public List<User> users;

    public List<Authtoken> authTokens;

    public ArrayList<Game> games;

    private DatabaseTEMP(){

        users = new ArrayList<>();
        authTokens = new ArrayList<>();
        games = new ArrayList<>();
    };

    public static DatabaseTEMP getDatabase(){

        if(database == null){
            database = new DatabaseTEMP();
        }

        return database;
    }

    public void clear(){

        database.users = new ArrayList<>();
        database.authTokens = new ArrayList<>();
        database.games = new ArrayList<>();
    }
}
