package ui;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import com.google.gson.Gson;
import webSocket.ServerFacade;

import java.io.*;
import java.util.Scanner;

public class PreLogin {

    private static final Gson gson = new Gson();
    private static String authToken;

    public static void preLogin() {

        while(true) {

            listMenuOptions();
            String userInput = getUserInput();

            if(userInput.equals("quit")){

                break;
            }else if(userInput.equals("login")){

                authToken = login();

                if(authToken != null) {

                    String logoutOrQuit = PostLogin.postLogin(authToken);

                    if (logoutOrQuit.equals("quit")) {

                        break;
                    }
                }

            }else if(userInput.equals("register")){

                authToken = register();

                if(authToken != null) {

                    String logoutOrQuit = PostLogin.postLogin(authToken);

                    if(logoutOrQuit.equals("quit")){

                        break;
                    }
                }
            }else if(userInput.equals("cleardatabase")){

                clear();
            }
        }
    }

    private static void listMenuOptions(){

        System.out.print("\nType \"register\" to create a new user account\n");
        System.out.print("Type \"login\" to...well...login\n");
        System.out.print("Type \"quit\" to end the program\n");
        System.out.print("Type \"help\" to see the menu options again\n\n");
        System.out.print("[LOGGED_OUT] >>> ");
    }

    private static String getUserInput(){

        Scanner myObj = new Scanner(System.in);
        String userInput = myObj.nextLine();
        System.out.print("\n");
        return userInput.toLowerCase();
    }

    private static String register() {

        System.out.print("Username: ");
        String username = getUserInput();
        System.out.print("Password: ");
        String password = getUserInput();
        System.out.print("Email: ");
        String email = getUserInput();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setEmail(email);

        String jsonLoginRequest = gson.toJson(registerRequest);

        try {
            String jsonResponse = ServerFacade.post(null, jsonLoginRequest, "/user");
            LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
            System.out.print("Logged in as " + loginResponse.getUsername() + "\n");
            return loginResponse.getAuthtoken();

        } catch (IOException | RuntimeException e) {

            System.out.print(e.getMessage() + "\n");
            return null;
        }
    }

    private static String login(){

        System.out.print("Username: ");
        String username = getUserInput();
        System.out.print("Password: ");
        String password = getUserInput();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        String jsonLoginRequest = gson.toJson(loginRequest);

        try {
            String jsonResponse = ServerFacade.post(null, jsonLoginRequest, "/session");
            LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
            System.out.print("Logged in as " + loginResponse.getUsername() + "\n");
            return loginResponse.getAuthtoken();

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
            return null;
        }
    }

    private static void clear(){

        try {
            ServerFacade.delete(null, "/db");
            System.out.print("Cleared entire database" + "\n");

        } catch (IOException e) {

            System.out.print(e.getMessage() + "\n");
        }
    }
}
