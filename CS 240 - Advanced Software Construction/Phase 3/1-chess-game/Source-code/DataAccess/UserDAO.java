package DataAccess;

import DatabaseTEMP.DatabaseTEMP;
import Models.Authtoken;
import Models.User;

public class UserDAO {

    private static final DatabaseTEMP database = DatabaseTEMP.getDatabase();

    public static void createUser(User user) throws DataAccessException{

        if(UserDAO.getUser(user) == null){
            database.users.add(user);
        }else{
            throw new DataAccessException();

        }
    }

    public static User getUser(User user){

        String findUsername = user.getUsername();

        for(int i = 0; i < database.users.size(); i++){

            User currentUser = database.users.get(i);

            if(currentUser.getUsername().equals(findUsername)){

                return currentUser;
            }
        }

        return null;
    }
}
