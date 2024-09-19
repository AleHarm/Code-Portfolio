package chess;

public class ChessBoardImple implements ChessBoard {

    ChessPiece[][] pieces = new ChessPiece[8][8];

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        pieces[row][col] = piece;
    }


    @Override
    public ChessPiece getPiece(ChessPosition position) {

        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        return pieces[row][col];
    }

    @Override
    public void resetBoard() {

        pieces[0][0] = new Rook(ChessGame.TeamColor.WHITE);
        pieces[0][1] = new Knight(ChessGame.TeamColor.WHITE);
        pieces[0][2] = new Bishop(ChessGame.TeamColor.WHITE);
        pieces[0][3] = new Queen(ChessGame.TeamColor.WHITE);
        pieces[0][4] = new King(ChessGame.TeamColor.WHITE);
        pieces[0][5] = new Bishop(ChessGame.TeamColor.WHITE);
        pieces[0][6] = new Knight(ChessGame.TeamColor.WHITE);
        pieces[0][7] = new Rook(ChessGame.TeamColor.WHITE);
        pieces[1][0] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][1] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][2] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][3] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][4] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][5] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][6] = new Pawn(ChessGame.TeamColor.WHITE);
        pieces[1][7] = new Pawn(ChessGame.TeamColor.WHITE);

        for(int i = 2; i < 6; i++){

            for(int j = 0; j < 8; j++){

                pieces[i][j] = null;
            }
        }

        pieces[6][0] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][1] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][2] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][3] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][4] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][5] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][6] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[6][7] = new Pawn(ChessGame.TeamColor.BLACK);
        pieces[7][0] = new Rook(ChessGame.TeamColor.BLACK);
        pieces[7][1] = new Knight(ChessGame.TeamColor.BLACK);
        pieces[7][2] = new Bishop(ChessGame.TeamColor.BLACK);
        pieces[7][3] = new Queen(ChessGame.TeamColor.BLACK);
        pieces[7][4] = new King(ChessGame.TeamColor.BLACK);
        pieces[7][5] = new Bishop(ChessGame.TeamColor.BLACK);
        pieces[7][6] = new Knight(ChessGame.TeamColor.BLACK);
        pieces[7][7] = new Rook(ChessGame.TeamColor.BLACK);
    }
}
