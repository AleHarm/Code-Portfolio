package passoffTests.UnitTests;

import DataAccess.*;
import Models.Authtoken;
import Models.Game;
import Models.User;
import Services.ClearService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class DAOTests {


    static Database database = Database.getInstance();
    static Connection conn;
    static GsonBuilder builder = new GsonBuilder();
    static Authtoken authToken;
    static User user;
    static Game game;
    static String authtokenString;
    static String usernameString = "Kentucky";
    static String passwordString = "aTerriblePassword";
    static String emailString = "email@email.email";
    static String gameNameString = "imBadAtChess";


    //
    //SETUP
    //

    @BeforeAll
    public static void setupAll() {

        try {
            conn = database.getConnection();
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @BeforeEach
    public void setup() {

        ClearService.clear();
        authtokenString = UUID.randomUUID().toString();
        authToken = new Authtoken(authtokenString, usernameString);
        user = new User(usernameString, passwordString, emailString);
        game = new Game(gameNameString);

        try {
            AuthDAO.createAuthtoken(authToken);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try {
            UserDAO.createUser(user);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try {
            GameDAO.insert(game);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }


    //
    //AuthDAO TESTS
    //

    @Test
    @DisplayName("Create a valid authtoken")
    public void createValidAuthtoken() {


        try (var preparedStatement = conn.prepareStatement("SELECT * FROM authtokens WHERE authtoken = ?")) {
            preparedStatement.setString(1, authToken.getAuthToken());
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var authtoken = rs.getString("authtoken");
                    var username = rs.getString("username");

                    Assertions.assertEquals(authtokenString, authtoken);
                    Assertions.assertEquals(usernameString, username);
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create a same authtoken twice")
    public void createAuthInvalidAuthtoken() {

        try {
            AuthDAO.createAuthtoken(authToken);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Authtoken could not be created in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Get a valid authtoken")
    public void getValidAuthtoken(){

        try {
            Authtoken newAuthtoken = AuthDAO.getAuthtoken(authToken);
            Assertions.assertEquals(authtokenString, newAuthtoken.getAuthToken());
            Assertions.assertEquals(usernameString, newAuthtoken.getUsername());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Get an invalid authtoken")
    public void getInvalidAuthtoken(){

        authToken = new Authtoken("notTheThing", usernameString);

        try {
            Authtoken newAuthtoken = AuthDAO.getAuthtoken(authToken);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Authtoken could not be found in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete valid authtoken")
    public void deleteValidAuthtoken() {

        try {
            AuthDAO.deleteAuthToken(authToken);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try (var conn = database.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM authtokens")) {
                try (var rs = preparedStatement.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                    }

                    Assertions.assertEquals(0, count);
                }
            }
        } catch (SQLException | DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Delete nonexistent authtoken")
    public void deleteInvalidAuthtoken() {

        authToken = new Authtoken(UUID.randomUUID().toString(), usernameString);

        try {
            AuthDAO.deleteAuthToken(authToken);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Authtoken could not be found in or deleted from database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Clear Authtokens")
    public void clearAuthtokens() {

        authToken = new Authtoken(UUID.randomUUID().toString(), "random");
        try {
            AuthDAO.createAuthtoken(authToken);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        authToken = new Authtoken(UUID.randomUUID().toString(), "test");
        try {
            AuthDAO.createAuthtoken(authToken);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try {
            AuthDAO.clear();
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try (var conn = database.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM authtokens")) {
                try (var rs = preparedStatement.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                    }

                    Assertions.assertEquals(0, count);
                }
            }
        } catch (SQLException | DataAccessException e) {
            Assertions.fail();
        }
    }


    //
    //UserDAO TESTS
    //

    @Test
    @DisplayName("Create a valid user")
    public void createValidUser() {

        try (var conn = database.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                preparedStatement.setString(1, user.getUsername());
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var username = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");


                        Assertions.assertEquals(usernameString, username);
                        Assertions.assertEquals(passwordString, password);
                        Assertions.assertEquals(emailString, email);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create a valid user")
    public void createInvalidUser() {

        try {
            UserDAO.createUser(user);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: User could not be created in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Get a valid user")
    public void getValidUser(){

        try {
            User newUser = UserDAO.getUser(user);
            Assertions.assertEquals(usernameString, newUser.getUsername());
            Assertions.assertEquals(passwordString, newUser.getPassword());
            Assertions.assertEquals(emailString, newUser.getEmail());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Get an invalid user")
    public void getInvalidUser(){

        user = new User("jeb", passwordString, emailString);

        try {
            User newUser = UserDAO.getUser(user);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: User could not be found in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Clear Users")
    public void clearUsers() {

        user = new User("test1", passwordString, emailString);
        try {
            UserDAO.createUser(user);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        user = new User("test2", passwordString, emailString);
        try {
            UserDAO.createUser(user);
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try {
            UserDAO.clear();
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        try (var conn = database.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM users")) {
                try (var rs = preparedStatement.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                    }

                    Assertions.assertEquals(0, count);
                }
            }
        } catch (SQLException | DataAccessException e) {
            Assertions.fail();
        }
    }


    //
    //GameDAO Tests
    //

    @Test
    @DisplayName("Create a valid game")
    public void createValidGame() {

        try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameName = ?")) {
            preparedStatement.setString(1, game.getGameName());
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var gameName = rs.getString("gameName");
                    var gameString = rs.getString("game");

                    Gson gson = new Gson();

                    Assertions.assertEquals(gameNameString, gameName);
                    Assertions.assertEquals(gson.toJson(game.getGame()), gameString);
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create an invalid game")
    public void createInvalidGame() {

        game = new Game(null);

        try {
            GameDAO.insert(game);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Game could not be created in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Find a valid game")
    public void findValidGame() {

        try {
            Game game = GameDAO.find(1);
            Assertions.assertEquals(gameNameString, game.getGameName());
            Assertions.assertEquals(1, game.getGameID());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Find an invalid game")
    public void findInvalidGame() {

        try {
            Game game = GameDAO.find(5);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Game could not be found in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Lists all games")
    public void listAllGames() {

        try {
            ArrayList<Game> games = GameDAO.findAll();
            Assertions.assertEquals(1, games.size());
            GameDAO.insert(game);
            games = GameDAO.findAll();
            Assertions.assertEquals(2, games.size());

        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Lists no games")
    public void listNoGames() {

        try {
            GameDAO.clear();
            ArrayList<Game> games = GameDAO.findAll();
            Assertions.assertEquals(0, games.size());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Update a valid game")
    public void updateValidGame() {

        try {
            Game game = GameDAO.find(1);
            game.setWhiteUsername("Chadbert");
            game.setBlackUsername("Filhelm");
            GameDAO.updateGame(1, game);
            game = GameDAO.find(1);
            Assertions.assertEquals("Chadbert", game.getWhiteUsername());
            Assertions.assertEquals("Filhelm", game.getBlackUsername());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Update an invalid game")
    public void updateInvalidGame() {

        try {
            GameDAO.updateGame(5, game);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Game could not be updated in database", e.getMessage());
        }
    }
}