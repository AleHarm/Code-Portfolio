package Services.Responses;

public class LoginResponse extends Response{

    private String authToken;
    private String username;

    public LoginResponse(){

    }

    public String getAuthtoken() {
        return authToken;
    }

    public void setAuthtoken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
