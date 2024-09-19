package DataAccess;

import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private final static Database database = Database.getInstance();

    public static void createUser(User user) throws DataAccessException{

        Connection conn = database.getConnection();

        try{
            UserDAO.getUser(user);
            throw new DataAccessException("Error: User could not be created in database");
        } catch (DataAccessException e) {
            try {
                String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getEmail());
                if (statement.executeUpdate() != 1) {
                    throw new DataAccessException("Error: User could not be created in database");
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Error: User could not be created in database");
            } finally {
                database.returnConnection(conn);
            }
        }
    }

    public static User getUser(User user) throws DataAccessException {

        Connection conn = database.getConnection();

        try {
            String sql = "SELECT * FROM users WHERE username = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()){
                    String usernameString = rs.getString("username");
                    String passwordString = rs.getString("password");
                    String emailString = rs.getString("email");

                    return new User(usernameString, passwordString, emailString);
                }
                throw new DataAccessException("Error: User could not be found in database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: User could not be found in database");
        }finally{
            database.returnConnection(conn);
        }
    }

    public static void clear() throws DataAccessException {

        Connection conn = database.getConnection();

        try {
            String sql = "TRUNCATE TABLE users";
            PreparedStatement statement = conn.prepareStatement(sql);
            if (statement.executeUpdate() != 0) {
                throw new DataAccessException("Error: Could not clear users table");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Could not clear users table");
        } finally {
            database.returnConnection(conn);
        }
    }
}
