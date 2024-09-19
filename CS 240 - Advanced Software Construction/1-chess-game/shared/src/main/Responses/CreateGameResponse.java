package Responses;

public class CreateGameResponse extends Response{

    Integer gameID = null;

    public CreateGameResponse() {
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID(){ return this.gameID;}
}
