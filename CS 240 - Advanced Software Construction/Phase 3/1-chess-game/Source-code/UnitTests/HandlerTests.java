package UnitTests;

import DatabaseTEMP.DatabaseTEMP;
import Models.Authtoken;
import Services.*;
import Services.Requests.CreateGameRequest;
import Services.Requests.JoinGameRequest;
import Services.Requests.LoginRequest;
import Services.Requests.RegisterRequest;
import Services.Responses.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;


public class HandlerTests {

    Gson gson;
    DatabaseTEMP database = DatabaseTEMP.getDatabase();
    Authtoken authToken;

    //Requests
    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    CreateGameRequest createGameRequest;
    JoinGameRequest joinGameRequest;

    //Responses
    LoginResponse registerResponse;
    LoginResponse loginResponse;
    LogoutResponse logoutResponse;
    CreateGameResponse createGameResponse;
    ListGamesResponse listGamesResponse;
    JoinGameResponse joinGameResponse;


    @BeforeEach
    public void setup() {

        gson = new Gson();
        database.clear();
        //Set requests
        registerRequest = new RegisterRequest();
        loginRequest = new LoginRequest();
        createGameRequest = new CreateGameRequest();
        joinGameRequest = new JoinGameRequest();


        //Set responses
        loginResponse = new LoginResponse();
        logoutResponse = new LogoutResponse();
        createGameResponse = new CreateGameResponse();
        listGamesResponse = new ListGamesResponse();
        joinGameResponse = new JoinGameResponse();

        //Add unique user to database
        registerRequest.setUsername("Jeff");
        registerRequest.setPassword("Jeff'sPassword123");
        registerRequest.setEmail("Jeff@email.com");
        registerResponse = RegisterService.register(registerRequest);

        authToken = new Authtoken(registerResponse.getAuthtoken(), null);
    }

    @Test
    @DisplayName("Register user")
    public void registerValidUser(){

        Assertions.assertEquals("Jeff", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotEquals(null, registerResponse.getAuthtoken());

        registerRequest.setUsername("Bill");
        registerRequest.setPassword("Bill123");
        registerRequest.setEmail("Bill@bill.bill");

        //Register a second unique user
        registerResponse =  RegisterService.register(registerRequest);
        Assertions.assertEquals("Bill", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotEquals(null, registerResponse.getAuthtoken());
    }

    @Test
    @DisplayName("Register user")
    public void registerInvalidUser() {

        Assertions.assertEquals("Jeff", registerResponse.getUsername());
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNull(registerResponse.getMessage());
        Assertions.assertNotEquals(null, registerResponse.getAuthtoken());

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
        Assertions.assertEquals(0, database.authTokens.size());
     }

    @Test
    @DisplayName("Logout a user that's already logged out")
    public void logoutInvalid() {
        //Logout user
        logoutResponse = LogoutService.logout(authToken);

        Assertions.assertTrue(logoutResponse.isSuccess());
        Assertions.assertNull(logoutResponse.getMessage());
        Assertions.assertEquals(0, database.authTokens.size());

        //Logout the logged-out user
        logoutResponse = LogoutService.logout(authToken);

        Assertions.assertFalse(logoutResponse.isSuccess());
        Assertions.assertNotNull(logoutResponse.getMessage());
    }
    @Test
    @DisplayName("Clear Database")
    public void clearDatabase(){

        Authtoken tempAuthtoken = new Authtoken("temp", null);
        database.authTokens.add(tempAuthtoken);


        //Clear single user
        ClearService.clear();
        Assertions.assertEquals(0, database.authTokens.size());
        Assertions.assertEquals(0, database.users.size());

        //Clear more than one user
        registerRequest.setUsername("Jeff");
        registerRequest.setPassword("Jeff'sPassword123");
        registerRequest.setEmail("Jeff@email.com");

        RegisterService.register(registerRequest);

        registerRequest.setUsername("Steve");
        registerRequest.setPassword("Steve'sPassword123");
        registerRequest.setEmail("Steve@email.com");

        RegisterService.register(registerRequest);

        database.authTokens.add(tempAuthtoken);

        ClearService.clear();
        Assertions.assertEquals(0, database.authTokens.size());
        Assertions.assertEquals(0, database.users.size());
        Assertions.assertEquals(0, database.games.size());

    }

    @Test
    @DisplayName("Clear empty Database")
    public void clearEmptyDatabase() {

        //Clear database
        ClearService.clear();
        Assertions.assertEquals(0, database.authTokens.size());
        Assertions.assertEquals(0, database.users.size());
        Assertions.assertEquals(0, database.games.size());

        Authtoken tempAuthtoken = new Authtoken("temp", null);
        database.authTokens.add(tempAuthtoken);

        //Clear empty database
        ClearService.clear();
        Assertions.assertEquals(0, database.authTokens.size());
        Assertions.assertEquals(0, database.users.size());
        Assertions.assertEquals(0, database.games.size());
    }

    @Test
    @DisplayName("Create valid game")
    public void createValidGame(){

        createGameRequest.setGameName("Christian Minecraft Server");

        createGameResponse = CreateGameService.createGame(createGameRequest, authToken);
        Assertions.assertEquals(1, database.games.size());
        Assertions.assertTrue(createGameResponse.isSuccess());
        Assertions.assertNull(createGameResponse.getMessage());
    }

    @Test
    @DisplayName("Create game with bad authToken")
    public void createInvalidGame(){

        Authtoken authTokenBad = new Authtoken("4", null);
        createGameRequest.setGameName("Christian Minecraft Server");

        createGameResponse = CreateGameService.createGame(createGameRequest, authTokenBad);
        Assertions.assertEquals(0, database.games.size());
        Assertions.assertFalse(createGameResponse.isSuccess());
        Assertions.assertNotNull(createGameResponse.getMessage());
    }

    @Test
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
    @DisplayName("List empty games list")
    public void listNoGames(){

        listGamesResponse = ListGamesService.listGames(authToken);

        Assertions.assertEquals(0, listGamesResponse.getGames().size());
        Assertions.assertTrue(listGamesResponse.isSuccess());
        Assertions.assertNull(listGamesResponse.getMessage());
    }

    @Test
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
