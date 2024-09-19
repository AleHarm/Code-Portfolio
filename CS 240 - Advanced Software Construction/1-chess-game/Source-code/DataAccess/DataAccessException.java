package DataAccess;

public class DataAccessException extends Exception {

    String message;

    public DataAccessException(String newMessage) {

        message = newMessage;
    }

    @Override
    public String getMessage(){

        return message;
    }
}
