package DataAccess;

import Models.Authtoken;
import DatabaseTEMP.DatabaseTEMP;

public class AuthDAO {

    private static final DatabaseTEMP database = DatabaseTEMP.getDatabase();

    public static void createAuthtoken(Authtoken authToken){

        database.authTokens.add(authToken);
    }

    public static Authtoken getAuthtoken(Authtoken authToken){

        for(int i = 0; i < database.authTokens.size(); i++){

            Authtoken currentAuthtoken = database.authTokens.get(i);

            if(currentAuthtoken.getAuthToken().equals(authToken.getAuthToken())){

                return currentAuthtoken;
            }
        }

        return null;
    }

    public static void deleteAuthToken(Authtoken authToken) throws DataAccessException{

        boolean tokenDeleted = false;

        for(int i = 0; i < database.authTokens.size(); i++){

            Authtoken currentAuthtoken = database.authTokens.get(i);

            if(currentAuthtoken.getAuthToken().equals(authToken.getAuthToken())){

                database.authTokens.remove(currentAuthtoken);
                tokenDeleted = true;
            }
        }

        if(!tokenDeleted){

            throw new DataAccessException();
        }
    }
}
