package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Models.Authtoken;
import Models.User;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import java.util.UUID;

public class RegisterService {

    public static LoginResponse register(RegisterRequest request){
        LoginResponse login = new LoginResponse();

        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());

        //Make sure request contains a password
        if(newUser.getPassword() == null){
            login.setSuccess(false);
            login.setMessage("Error: Bad request, no password");
            return login;
        }

        //Create user in database
        try {
            UserDAO.createUser(newUser);
        } catch (DataAccessException e) {
            login.setSuccess(false);
//            login.setMessage("Error: User could not be created in database");
            login.setMessage(e.getMessage());

            return login;
        }

        //Create authtoken and add to database
        Authtoken newAuthtoken = new Authtoken(UUID.randomUUID().toString(), request.getUsername());
        try {
            AuthDAO.createAuthtoken(newAuthtoken);
        } catch (DataAccessException e) {
            login.setSuccess(false);
//            login.setMessage("Error: AuthToken could not be created in database");
            login.setMessage(e.getMessage());

            return login;
        }

        //Return info
        login.setUsername(request.getUsername());
        login.setAuthtoken(newAuthtoken.getAuthToken());
        login.setSuccess(true);
        return login;
    }
}
