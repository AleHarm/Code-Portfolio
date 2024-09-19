package DataAccess;

import Models.Authtoken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    private final static Database database = Database.getInstance();


    public static void createAuthtoken(Authtoken authToken) throws DataAccessException{

        Connection conn = database.getConnection();

        try {
            String sql = "INSERT INTO authtokens (authtoken, username) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, authToken.getAuthToken());
            statement.setString(2, authToken.getUsername());
            if (statement.executeUpdate() != 1) {
                throw new DataAccessException("Error: Authtoken could not be created in database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Authtoken could not be created in database");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static Authtoken getAuthtoken(Authtoken authToken) throws DataAccessException{

        Connection conn = database.getConnection();

        try {
            String sql = "SELECT * FROM authtokens WHERE authtoken = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, authToken.getAuthToken());
            try (ResultSet rs = statement.executeQuery()) {

                if(rs.next()){
                    String authtokenString = rs.getString("authtoken");
                    String usernameString = rs.getString("username");

                    return new Authtoken(authtokenString, usernameString);
                }
                throw new DataAccessException("Error: Authtoken could not be found in database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Authtoken could not be found in database");
        }
    }

    public static void deleteAuthToken(Authtoken authToken) throws DataAccessException{

        Connection conn = database.getConnection();

        try {
            String sql = "DELETE FROM authtokens WHERE authtoken = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, authToken.getAuthToken());
            if(statement.executeUpdate() == 0){
                throw new DataAccessException("Error: Authtoken could not be found in or deleted from database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Authtoken could not be found in or deleted from database");
        }
    }

    public static void clear() throws DataAccessException {

        Connection conn = database.getConnection();

        try {
            String sql = "TRUNCATE TABLE authtokens";
            PreparedStatement statement = conn.prepareStatement(sql);
            if (statement.executeUpdate() != 0) {
                throw new DataAccessException("Error: Could not clear authtokens table");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Could not clear authtokens table");
        } finally {
            database.returnConnection(conn);
        }
    }
}
