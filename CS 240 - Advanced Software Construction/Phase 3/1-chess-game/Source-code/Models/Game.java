package Models;

import chess.ChessGame;
import chess.ChessGameImple;

/**
 * the class for the model of the game
 */
public class Game {

    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public Game(int gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.game = new ChessGameImple();
    }


    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

}
