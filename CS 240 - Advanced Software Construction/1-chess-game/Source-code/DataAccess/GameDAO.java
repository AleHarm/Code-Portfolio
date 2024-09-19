package DataAccess;

import Models.Game;
import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class GameDAO {

    private final static Database database = Database.getInstance();


    public static int insert(Game game) throws DataAccessException{

        Gson gson = new Gson();
        Connection conn = database.getConnection();

        try {
            String sql = "INSERT INTO games (gameName, game) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            String gameString = gson.toJson(game.getGame());
            statement.setString(1, game.getGameName());
            statement.setString(2, gameString);
            if (statement.executeUpdate() != 1) {
                throw new DataAccessException("Error: Game could not be created in database");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            throw new DataAccessException("Error: Game could not be created in database");
        } catch (SQLException e) {
            throw new DataAccessException("Error: Game could not be created in database");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static Game find(int gameID) throws DataAccessException{

        Connection conn = database.getConnection();

        try {
            String sql = "SELECT * FROM games WHERE gameID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, gameID);
            try (ResultSet rs = statement.executeQuery()) {

                if(rs.next()){
                    int gameIDString = rs.getInt("gameID");
                    String gameJSON = rs.getString("game");
                    String gameName = rs.getString("gameName");
                    String whiteUsernameString = rs.getString("whiteUsername");
                    String blackUsernameString = rs.getString("blackUsername");
                    Game game = new Game(gameName);
                    game.setGameID(gameIDString);
                    game.setWhiteUsername(whiteUsernameString);
                    game.setBlackUsername(blackUsernameString);
                    game.setGame(Deserialize(gameJSON));
                    return game;
                }
                throw new DataAccessException("Error: Game could not be found in database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Game could not be found in database");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static ArrayList<Game> findAll() throws DataAccessException {

        Connection conn = database.getConnection();
        ArrayList<Game> games = new ArrayList<>();

        try {
            String sql = "SELECT * FROM games";
            PreparedStatement statement = conn.prepareStatement(sql);
            try (ResultSet rs = statement.executeQuery()) {

                while(rs.next()){
                    int gameIDString = rs.getInt("gameID");
                    String gameJSON = rs.getString("game");
                    String gameName = rs.getString("gameName");
                    String whiteUsernameString = rs.getString("whiteUsername");
                    String blackUsernameString = rs.getString("blackUsername");
                    Game game = new Game(gameName);
                    game.setGameID(gameIDString);
                    game.setWhiteUsername(whiteUsernameString);
                    game.setBlackUsername(blackUsernameString);
                    //game.setGame(Deserialize(gameJSON));
                    game.setGame(null);
                    games.add(game);
                }

                return games;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Something went wrong finding all games");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static void updateGame(int gameID, Game game) throws DataAccessException{

        Gson gson = new Gson();
        Connection conn = database.getConnection();

        try {
            String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            String gameString = gson.toJson(game.getGame());
            statement.setString(1, game.getWhiteUsername());
            statement.setString(2, game.getBlackUsername());
            statement.setString(3, game.getGameName());
            statement.setString(4, gameString);
            statement.setInt(5, gameID);
            if (statement.executeUpdate() != 1) {
                throw new DataAccessException("Error: Game could not be updated in database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Game could not be updated in database");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static void clear() throws DataAccessException {

        Connection conn = database.getConnection();

        try {
            String sql = "TRUNCATE TABLE games";
            PreparedStatement statement = conn.prepareStatement(sql);
            if (statement.executeUpdate() != 0) {
                throw new DataAccessException("Error: Could not clear games table");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Could not clear games table");
        } finally {
            database.returnConnection(conn);
        }
    }

    public static ChessGame Deserialize(String game){

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(ChessGame.class, new GameAdapter())
                .registerTypeAdapter(ChessBoard.class, new BoardAdapter());

        return builder.create().fromJson(game, ChessGameImple.class);
    }

    private static class GameAdapter implements JsonDeserializer<ChessGame>{

        @Override
        public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            return new Gson().fromJson(jsonElement, ChessGameImple.class);
        }
    }

    private static class BoardAdapter implements JsonDeserializer<ChessBoard>{


        @Override
        public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            GsonBuilder builder = new GsonBuilder()
                    .registerTypeAdapter(ChessPieceImple.class, new ChessPieceAdapter());

            return builder.create().fromJson(jsonElement, ChessBoardImple.class);
        }
    }

    private static class ChessPieceAdapter implements JsonDeserializer<ChessPiece>{


        @Override
        public ChessPieceImple deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            String pieceType = jsonElement.getAsJsonObject().get("type").getAsString();


            if(Objects.equals(pieceType, "KING")){
                return new Gson().fromJson(jsonElement, King.class);

            }else if(Objects.equals(pieceType, "KNIGHT")){
                return new Gson().fromJson(jsonElement, Knight.class);

            }else if(Objects.equals(pieceType, "PAWN")){
                return new Gson().fromJson(jsonElement, Pawn.class);

            }else if(Objects.equals(pieceType, "QUEEN")){
                return new Gson().fromJson(jsonElement, Queen.class);

            }else{
                return new Gson().fromJson(jsonElement, Rook.class);

            }
        }
    }

}
