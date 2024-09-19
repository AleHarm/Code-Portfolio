package Services;

import DataAccess.AuthDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Models.Authtoken;
import Models.User;
import Requests.LoginRequest;
import Responses.LoginResponse;
import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public static LoginResponse login(LoginRequest request){
        LoginResponse loginResponse = new LoginResponse();

        //Make sure the user exists
        User user = new User(request.getUsername(), request.getPassword(), null);
        User dbUser;

        try{
            dbUser = UserDAO.getUser(user);
        } catch (DataAccessException e) {
            loginResponse.setSuccess(false);
            loginResponse.setMessage(e.getMessage());
            return loginResponse;
        }

        //Check if password is correct
        if(!Objects.equals(user.getPassword(), dbUser.getPassword())){
            loginResponse.setSuccess(false);
            loginResponse.setMessage("Error: Incorrect password");
            return loginResponse;
        }



        //Make authToken
        Authtoken newAuthtoken = new Authtoken(UUID.randomUUID().toString(), request.getUsername());
        try {
            AuthDAO.createAuthtoken(newAuthtoken);
        } catch (DataAccessException e) {
            loginResponse.setSuccess(false);
//            loginResponse.setMessage("Error: Could not insert AuthToken");
            loginResponse.setMessage(e.getMessage());
            return loginResponse;
        }

        //Return info
        loginResponse.setUsername(request.getUsername());
        loginResponse.setAuthtoken(newAuthtoken.getAuthToken());
        loginResponse.setSuccess(true);
        return loginResponse;
    }
}
