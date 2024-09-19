package passoffTests.UnitTests;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Models.Authtoken;
import Responses.CreateGameResponse;
import Responses.ListGamesResponse;
import Responses.LoginResponse;
import Responses.Response;
import Services.*;
import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HandlerTests {

    Gson gson;
    private final static Database database = Database.getInstance();


    Authtoken authToken;

    //Requests
    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    CreateGameRequest createGameRequest;
    JoinGameRequest joinGameRequest;

    //Responses
    LoginResponse registerResponse;
    LoginResponse loginResponse;
    Response logoutResponse;
    CreateGameResponse createGameResponse;
    ListGamesResponse listGamesResponse;
    Response joinGameResponse;
    static Connection conn;



    @BeforeAll
    public static void setup1(){

        try {
            conn = database.getConnection();
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @BeforeEach
    public void setup() {

        gson = new Gson();
        ClearService.clear();
        //Set requests
        registerRequest = new RegisterRequest();
        loginRequest = new LoginRequest();
        createGameRequest = new CreateGameRequest();
        joinGameRequest = new JoinGameRequest();


        //Set responses
        loginResponse = new LoginResponse();
        logoutResponse = new Response();
        createGameResponse = new CreateGameResponse();
        listGamesResponse = new ListGamesResponse();
        joinGameResponse = new Response();

        //Add unique user to database
        registerRequest.setUsername("Jeff");
        registerRequest.setPassword("Jeff'sPassword123");
        registerRequest.setEmail("Jeff@email.com");
        registerResponse = RegisterService.register(registerRequest);

        authToken = new Authtoken(registerResponse.getAuthtoken(), null);
    }

    @Test
    @Order(1)
    @DisplayName("Register user")
    public void registerValidUser(){

        Assertions.assertEquals("Jeff", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotNull(registerResponse.getAuthtoken());

        registerRequest.setUsername("Bill");
        registerRequest.setPassword("Bill123");
        registerRequest.setEmail("Bill@bill.bill");

        //Register a second unique user
        registerResponse =  RegisterService.register(registerRequest);
        Assertions.assertEquals("Bill", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotNull(registerResponse.getAuthtoken());
    }

    @Test
    @Order(2)
    @DisplayName("Register invalid user")
    public void registerInvalidUser() {

        Assertions.assertEquals("Jeff", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotNull(registerResponse.getAuthtoken());

        //Add same user again
        registerResponse = RegisterService.register(registerRequest);
        Assertions.assertNull(registerResponse.getUsername());
        Assertions.assertFalse(registerResponse.isSuccess());
        Assertions.assertEquals("Error: User could not be created in database", registerResponse.getMessage());
        Assertions.assertNull(registerResponse.getAuthtoken());

        registerRequest.setUsername("Bill");
        registerRequest.setPassword("Bill123");
        registerRequest.setEmail("Bill@bill.bill");

        registerRequest.setUsername(null);
        registerRequest.setPassword(null);
        registerRequest.setEmail(null);

        //Add unique user
        registerResponse = RegisterService.register(registerRequest);

        Assertions.assertNull(registerResponse.getUsername());
        Assertions.assertFalse(registerResponse.isSuccess());
        Assertions.assertEquals("Error: Bad request, no password", registerResponse.getMessage());
        Assertions.assertNull(registerResponse.getAuthtoken());
    }
    @Test
    @Order(3)
    @DisplayName("Login user")
    public void loginValidUser(){

        //Login as valid user
        loginRequest = new LoginRequest();
        loginRequest.setUsername("Jeff");
        loginRequest.setPassword("Jeff'sPassword123");

        loginResponse = LoginService.login(loginRequest);

        Assertions.assertEquals("Jeff", loginResponse.getUsername());
        Assertions.assertTrue(loginResponse.isSuccess());
        Assertions.assertNull(loginResponse.getMessage());
        Assertions.assertNotEquals(null, loginResponse.getAuthtoken());

        //Login as same user again
        loginResponse = LoginService.login(loginRequest);

        Assertions.assertEquals("Jeff", loginResponse.getUsername());
        Assertions.assertTrue(loginResponse.isSuccess());
        Assertions.assertNull(loginResponse.getMessage());
        Assertions.assertNotEquals(null, loginResponse.getAuthtoken());
    }

    @Test
    @Order(4)
    @DisplayName("Login user")
    public void loginInvalidUser() {
        //Login as non-existent user
        loginRequest.setUsername("NotInTheDatabase");
        loginRequest.setPassword("ThisPasswordWontWork12345");

        loginResponse = LoginService.login(loginRequest);
        Assertions.assertNull(loginResponse.getUsername());
        Assertions.assertFalse(loginResponse.isSuccess());
        Assertions.assertEquals("Error: User could not be found in database", loginResponse.getMessage());
        Assertions.assertNull(loginResponse.getAuthtoken());
    }

    @Test
    @DisplayName("Logout valid user")
    public void logoutValid(){
        //Logout user
        logoutResponse =  LogoutService.logout(authToken);

        Assertions.assertTrue(logoutResponse.isSuccess());
        Assertions.assertNull(logoutResponse.getMessage());
     }

    @Test
    @Order(5)
    @DisplayName("Logout a user that's already logged out")
    public void logoutInvalid() {
        //Logout user
        logoutResponse = LogoutService.logout(authToken);

        Assertions.assertTrue(logoutResponse.isSuccess());
        Assertions.assertNull(logoutResponse.getMessage());

        //Logout the logged-out user
        logoutResponse = LogoutService.logout(authToken);

        Assertions.assertFalse(logoutResponse.isSuccess());
        Assertions.assertNotNull(logoutResponse.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("Clear Database")
    public void clearDatabase(){

        Authtoken tempAuthtoken = new Authtoken("temp", null);


        //Clear single user
        ClearService.clear();

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

        //Clear more than one user
        registerRequest.setUsername("Jeff");
        registerRequest.setPassword("Jeff'sPassword123");
        registerRequest.setEmail("Jeff@email.com");

        RegisterService.register(registerRequest);

        registerRequest.setUsername("Steve");
        registerRequest.setPassword("Steve'sPassword123");
        registerRequest.setEmail("Steve@email.com");

        RegisterService.register(registerRequest);

        ClearService.clear();

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
    @Order(7)
    @DisplayName("Clear empty Database")
    public void clearEmptyDatabase() {

        //Clear database
        ClearService.clear();

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

        Authtoken tempAuthtoken = new Authtoken("temp", null);

        //Clear empty database
        ClearService.clear();

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
    @Order(8)
    @DisplayName("Create valid game")
    public void createValidGame(){

        createGameRequest.setGameName("Christian Minecraft Server");

        createGameResponse = CreateGameService.createGame(createGameRequest, authToken);
        Assertions.assertTrue(createGameResponse.isSuccess());
        Assertions.assertNull(createGameResponse.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("Create game with bad authToken")
    public void createInvalidGame(){

        Authtoken authTokenBad = new Authtoken("4", null);
        createGameRequest.setGameName("Christian Minecraft Server");

        createGameResponse = CreateGameService.createGame(createGameRequest, authTokenBad);
        Assertions.assertFalse(createGameResponse.isSuccess());
        Assertions.assertNotNull(createGameResponse.getMessage());
    }

    @Test
    @Order(11)
    @DisplayName("List multiple games")
    public void listGames(){

        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);
        createGameRequest.setGameName("Fish cakes");
        CreateGameService.createGame(createGameRequest, authToken);
        createGameRequest.setGameName("This one's a strange duck");
        CreateGameService.createGame(createGameRequest, authToken);

        listGamesResponse = ListGamesService.listGames(authToken);

        Assertions.assertEquals(3, listGamesResponse.getGames().size());
        Assertions.assertTrue(listGamesResponse.isSuccess());
        Assertions.assertNull(listGamesResponse.getMessage());
    }

    @Test
    @Order(12)
    @DisplayName("List empty games list")
    public void listNoGames(){

        listGamesResponse = ListGamesService.listGames(authToken);

        Assertions.assertEquals(0, listGamesResponse.getGames().size());
        Assertions.assertTrue(listGamesResponse.isSuccess());
        Assertions.assertNull(listGamesResponse.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("Join game as white")
    public void joinGameWhite(){
        //Create game
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);

        //Make joinGameRequest
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("WHITE");

        //Join game
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);
        Assertions.assertTrue(joinGameResponse.isSuccess());
        Assertions.assertNull(joinGameResponse.getMessage());
    }

    @Test
    @Order(14)
    @DisplayName("Invalid join: white player already assigned")
    public void joinGameWhiteInvalid(){
        //Valid join as white
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("WHITE");
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);

        //Add second player
        registerRequest.setUsername("Steve");
        registerRequest.setPassword("Steve'sPassword123");
        registerRequest.setEmail("Steve@email.com");
        Authtoken stevesToken = new Authtoken(RegisterService.register(registerRequest).getAuthtoken(), "Steve");

        //Second player try to join white, failure
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, stevesToken);
        Assertions.assertFalse(joinGameResponse.isSuccess());
        Assertions.assertNotNull(joinGameResponse.getMessage());
    }

    @Test
    @Order(15)
    @DisplayName("Join game as black")
    public void joinGameBlack(){
        //Create game
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);

        //Make joinGameRequest
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("BLACK");

        //Join game
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);
        Assertions.assertTrue(joinGameResponse.isSuccess());
        Assertions.assertNull(joinGameResponse.getMessage());
    }

    @Test
    @Order(16)
    @DisplayName("Invalid join: black player already assigned")
    public void joinGameBlackInvalid(){
        //Valid join as white
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("BLACK");
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);

        //Add second player
        registerRequest.setUsername("Steve");
        registerRequest.setPassword("Steve'sPassword123");
        registerRequest.setEmail("Steve@email.com");
        Authtoken stevesToken = new Authtoken(RegisterService.register(registerRequest).getAuthtoken(), "Steve");

        //Second player try to join black, failure
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, stevesToken);
        Assertions.assertFalse(joinGameResponse.isSuccess());
        Assertions.assertNotNull(joinGameResponse.getMessage());
    }

    @Test
    @Order(17)
    @DisplayName("Join game as watcher")
    public void joinGameWatcher(){
        //Create game
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authToken);

        //Make joinGameRequest
        joinGameRequest.setGameID(1);

        //Join game
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);
        Assertions.assertTrue(joinGameResponse.isSuccess());
        Assertions.assertNull(joinGameResponse.getMessage());
    }

    @Test
    @Order(18)
    @DisplayName("Join game with bad authToken")
    public void joinGameInvalid(){
        //Create invalid authToken
        Authtoken authTokenBad = new Authtoken("I'm Not Legit", null);

        //Create game
        createGameRequest.setGameName("Christian Minecraft Server");
        CreateGameService.createGame(createGameRequest, authTokenBad);

        //Make joinGameRequest
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("WHITE");

        //Join game
        joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);
        Assertions.assertFalse(joinGameResponse.isSuccess());
        Assertions.assertNotNull(joinGameResponse.getMessage());
    }
}
