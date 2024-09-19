package webSocket;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private static final String baseURL = "http://localhost:8080";

    public static String post(String authToken, String request, String path) throws IOException {
        URL url = new URL(baseURL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.addRequestProperty("authorization", "text/plain");
        connection.setRequestProperty("authorization", authToken);


        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(request.getBytes());
        }

        InputStream responseBody = connection.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    public static String delete(String authToken, String path) throws IOException {
        URL url = new URL(baseURL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        connection.addRequestProperty("authorization", "text/plain");
        connection.setRequestProperty("authorization", authToken);

        connection.connect();

        InputStream responseBody = connection.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    public static String get(String authToken, String path) throws IOException {
        URL url = new URL(baseURL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        connection.addRequestProperty("authorization", "text/plain");
        connection.setRequestProperty("authorization", authToken);


        connection.connect();

        InputStream responseBody = connection.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    public static String put(String authToken, String request, String path) throws IOException {
        URL url = new URL(baseURL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        connection.addRequestProperty("authorization", "text/plain");
        connection.setRequestProperty("authorization", authToken);


        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(request.getBytes());
        }

        InputStream responseBody = connection.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }
}
