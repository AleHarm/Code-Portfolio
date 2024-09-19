import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Requests.RegisterRequest;
import Responses.ListGamesResponse;
import Responses.LoginResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import webSocket.ServerFacade;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    static Gson gson = new Gson();
    static String authToken = null;

    @BeforeAll
    public static void setup(){

        try {
            ServerFacade.delete(null, "/db");
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Invalid GET (Null AuthToken)")
    public void invalidGET() {

        try {
            ServerFacade.get(authToken,  "/game");
            Assertions.fail();
        } catch (IOException e) {
            Assertions.assertTrue(true);
        }
    }


    @Test
    @Order(2)
    @DisplayName("Invalid DELETE (Null AuthToken)")
    public void invalidDELETE() {

        try {
            ServerFacade.delete(authToken, "/session");
            Assertions.fail();
        } catch (IOException e) {

            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Invalid PUT (Null AuthToken)")
    public void invalidPUT() {

        JoinGameRequest joinGameRequest = new JoinGameRequest();
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("white");

        String jsonJoinGameRequest = gson.toJson(joinGameRequest);

        try {
            ServerFacade.put(authToken, jsonJoinGameRequest, "/game");
            Assertions.fail();

        } catch (IOException e) {

            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Valid POST (Register User)")
    public void validPOST() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("John");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@email.email");

        String jsonLoginRequest = gson.toJson(registerRequest);

        try {
            String jsonResponse = ServerFacade.post(null, jsonLoginRequest, "/user");
            LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
            authToken = loginResponse.getAuthtoken();
            Assertions.assertNotNull(authToken);

            CreateGameRequest createGameRequest = new CreateGameRequest();
            createGameRequest.setGameName("jeff");

            String jsonCreateGameRequest = gson.toJson(createGameRequest);

            try {
                ServerFacade.post(authToken, jsonCreateGameRequest, "/game");

            } catch (IOException e) {

                Assertions.fail();
            }

        } catch (IOException | RuntimeException e) {

            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    @DisplayName("Invalid POST (Repeat register)")
    public void invalidPOST() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("John");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@email.email");

        String jsonLoginRequest = gson.toJson(registerRequest);

        try {
            ServerFacade.post(null, jsonLoginRequest, "/user");
            Assertions.fail();
        }catch (IOException e) {

            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Valid GET (List Games)")
    public void validGET() {

        try {
            String jsonResponse = ServerFacade.get(authToken,  "/game");
            ListGamesResponse listGamesResponse = gson.fromJson(jsonResponse, ListGamesResponse.class);
            Assertions.assertNotNull(listGamesResponse.getGames());
        } catch (IOException e) {

            Assertions.fail();
        }
    }

    @Test
    @Order(7)
    @DisplayName("Valid PUT (Add PLayer)")
    public void validPUT() {

        JoinGameRequest joinGameRequest = new JoinGameRequest();
        joinGameRequest.setGameID(1);
        joinGameRequest.setPlayerColor("white");

        String jsonJoinGameRequest = gson.toJson(joinGameRequest);

        try {
            ServerFacade.put(authToken, jsonJoinGameRequest, "/game");

        } catch (IOException e) {

            Assertions.fail();
        }
    }

    @Test
    @Order(8)
    @DisplayName("Valid DELETE (Logout)")
    public void validDELETE() {

        try {
            ServerFacade.delete(authToken, "/session");

        } catch (IOException e) {

            Assertions.fail();
        }
    }
}
