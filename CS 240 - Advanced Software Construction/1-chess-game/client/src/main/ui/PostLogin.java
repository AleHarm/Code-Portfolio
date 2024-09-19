package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Models.Game;
import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Responses.CreateGameResponse;
import Responses.ListGamesResponse;
import com.google.gson.Gson;
import webSocket.ServerFacade;

public class PostLogin {

    private static String authToken;
    private static final Gson gson = new Gson();
    private static Map<Integer, Object[]> gamesList = new HashMap<>();

    public static String postLogin(String newAuthToken){

        authToken = newAuthToken;

        while(true) {
            resetGamesList();
            listMenuOptions();
            String userInput = getUserInput();

            if(userInput.equals("quit")){

                return "quit";
            }else if(userInput.equals("logout")){

                if(isLogoutSuccess()){

                    return "logout";
                }
            }else if(userInput.equals("create")){

                System.out.print("Game name: ");
                String gameName = getUserInput();
                int gameID = createGame(gameName);

                if(gameID > 0){

                    resetGamesList();
                    gamesList.put(gamesList.size(), new Object[]{gameID, gameName});
                }

            }else if(userInput.equals("list")){

                gamesList.forEach((k, v) -> System.out.println((k + ": " + v[1] + "\n\tWhite Player: " + v[2] + "\n\tBlack Player: " + v[3])));
                System.out.print("\n");
            }else if(userInput.equals("join")){

                String gameIDString;

                while(true) {
                    System.out.print("Game ID: ");
                    gameIDString = getUserInput();
                    try {
                        int gameID = Integer.parseInt(gameIDString);

                        System.out.print("Specify a color: ");
                        String color = getUserInput();

                        while(!color.equals("white") && !color.equals("black")){

                            System.out.print("Error: Color must be \"white\" or \"black\"\n");
                            System.out.print("Specify a color: ");
                            color = getUserInput();
                        }

                        if(isAddPlayerSuccess(gameID, color)){
                            InGame.inGame();
                            removePlayer(gameID, color);
                        }
                        break;

                    } catch (NumberFormatException e) {
                        System.out.print("Error: GameID must be of type Integer\n");
                    }
                }

            }else if(userInput.equals("observe")){

                String gameIDString;

                while(true) {
                    System.out.print("Game ID: ");
                    gameIDString = getUserInput();
                    try {
                        int gameID = Integer.parseInt(gameIDString);
                        if(isAddPlayerSuccess(gameID, null)){

                            InGame.inGame();
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.print("Error: GameID must be of type Integer\n");
                    }
                }


            }else if(userInput.equals("cleardatabase")){

                if(isClearSuccess()){
                    return "logout";
                }
            }
        }
    }

    private static void listMenuOptions(){

        System.out.print("Type \"create\" to create a new game\n");
        System.out.print("Type \"list\" to list all games\n");
        System.out.print("Type \"join\" to join a game as a player\n");
        System.out.print("Type \"observe\" to observe a game\n");
        System.out.print("Type \"logout\" to...well...logout\n");
        System.out.print("Type \"quit\" to end the program\n");
        System.out.print("Type \"help\" to see the menu options again\n\n");
        System.out.print("[LOGGED_IN] >>> ");
    }

    private static String getUserInput(){

        Scanner myObj = new Scanner(System.in);
        String userInput = myObj.nextLine();
        System.out.print("\n");
        return userInput.toLowerCase();
    }

    private static boolean isLogoutSuccess(){

        try {
            ServerFacade.delete(authToken, "/session");
            System.out.print("Logged out\n");
            return true;

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return false;
        }
    }

    private static ArrayList<Game> listGames(){

        try {
            String jsonResponse = ServerFacade.get(authToken,  "/game");
            ListGamesResponse listGamesResponse = gson.fromJson(jsonResponse, ListGamesResponse.class);
            return listGamesResponse.getGames();

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return null;
        }
    }

    private static int createGame(String gameName){

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setGameName(gameName);

        String jsonCreateGameRequest = gson.toJson(createGameRequest);

        try {
            String jsonResponse = ServerFacade.post(authToken, jsonCreateGameRequest, "/game");
            CreateGameResponse createGameResponse = gson.fromJson(jsonResponse, CreateGameResponse.class);
            System.out.print("Game created with name " + gameName + "\n");
            return createGameResponse.getGameID();

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return -1;
        }
    }

    private static void resetGamesList() {

        ArrayList<Game> games = listGames();

        if(gamesList != null) {
            gamesList.clear();
        }
        if(games != null) {
            for (int i = 0; i < games.size(); i++) {

                gamesList.put( i + 1, new Object[]{games.get(i).getGameID(), games.get(i).getGameName(), games.get(i).getWhiteUsername(), games.get(i).getBlackUsername()});
            }
        }
    }

    private static boolean isClearSuccess(){


        try {
            ServerFacade.delete(null, "/db");
            System.out.print("Cleared entire database" + "\n");
            return true;

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return false;
        }
    }

    private static boolean isAddPlayerSuccess(int gameID, String color) {

        JoinGameRequest joinGameRequest = new JoinGameRequest();
        joinGameRequest.setGameID(gameID);
        joinGameRequest.setPlayerColor(color);

        String jsonJoinGameRequest = gson.toJson(joinGameRequest);

        try {
            ServerFacade.put(authToken, jsonJoinGameRequest, "/game");
            System.out.print("Joined game as: ");
            if(color != null){

                System.out.print(color + "\n");
            }else{

                System.out.print("observer\n");
            }

            return true;

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return false;
        }
    }

    private static void removePlayer(int gameID, String color) {

        JoinGameRequest joinGameRequest = new JoinGameRequest();
        joinGameRequest.setGameID(gameID);
        joinGameRequest.setPlayerColor("-" + color);

        String jsonJoinGameRequest = gson.toJson(joinGameRequest);

        try {
            ServerFacade.put(authToken, jsonJoinGameRequest, "/game");

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
        }
    }
}
