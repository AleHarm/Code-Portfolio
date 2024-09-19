package ChessClasses;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Pawn extends ChessPieceImple{

    private PieceType type;

    public Pawn(chess.ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.PAWN;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<chess.ChessMove> pieceMoves(ChessBoard board, chess.ChessPosition myPosition) {

        boolean firstMove;
        chess.ChessPiece piece= board.getPiece(myPosition);
        chess.ChessGame.TeamColor color = piece.getTeamColor();
        chess.ChessGame.TeamColor enemyColor;
        if(color == chess.ChessGame.TeamColor.WHITE){
            enemyColor = chess.ChessGame.TeamColor.BLACK;
        }else{
            enemyColor = chess.ChessGame.TeamColor.WHITE;
        }
        chess.ChessPosition testPos;
        Collection<chess.ChessMove> moves = new HashSet<>();

        if(piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE){
            if(myPosition.getRow() == 2){
                firstMove = true;
            }else{
                firstMove = false;
            }
        }else{
            if(myPosition.getRow() == 7){
                firstMove = true;
            }else{
                firstMove = false;
            }
        }

        //
        //
        //WHITE
        //
        //

        //CHECK IF THE NEXT ROW IS LESS THAN THE MAX ROW
        if((myPosition.getRow() + 1) <= 8 && color == chess.ChessGame.TeamColor.WHITE){

            //CHECK SPACE DIRECTLY IN FRONT
            testPos = new ChessPositionImple(myPosition.getRow() + 1, myPosition.getColumn());
            if(board.getPiece(testPos) == null){

                if(myPosition.getRow() + 1 == 8) {
                    AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                    AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                    AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                    AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                }else{

                    AddNewMove(moves, myPosition, testPos, null);

                    testPos = new ChessPositionImple(myPosition.getRow() + 2, myPosition.getColumn());

                    //FIRST MOVE, TWO SPACES
                    if(firstMove && board.getPiece(testPos) == null ){

                        AddNewMove(moves, myPosition, testPos, null);
                    }
                }
            }

            //CHECK IF RIGHT COLUMN IS INSIDE THE BOARD
            if((myPosition.getColumn() + 1) <= 8){

                testPos = new ChessPositionImple(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                chess.ChessPiece potentialEnemy = board.getPiece(testPos);

                if (potentialEnemy != null) {
                    if(potentialEnemy.getTeamColor() == enemyColor){

                        if(myPosition.getRow() + 1 == 8) {
                            AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                            AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                            AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                            AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                        }else {
                            AddNewMove(moves, myPosition, testPos, null);
                        }
                    }
                }
            }

            //CHECK IF LEFT COLUMN US INSIDE THE BOARD
            if((myPosition.getColumn() - 1) >= 1){

                testPos = new ChessPositionImple(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                chess.ChessPiece potentialEnemy = board.getPiece(testPos);

                if (potentialEnemy != null) {
                    if(potentialEnemy.getTeamColor() == enemyColor){

                        if(myPosition.getRow() + 1 == 8) {
                            AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                            AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                            AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                            AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                        }else {
                            AddNewMove(moves, myPosition, testPos, null);
                        }
                    }
                }
            }
        }


        //
        //
        //BLACK
        //
        //

        //CHECK IF THE NEXT ROW IS LESS THAN THE MAX ROW
        if((myPosition.getRow() - 1) >= 1 && color == ChessGame.TeamColor.BLACK){

            //CHECK SPACE DIRECTLY IN FRONT
            testPos = new ChessPositionImple(myPosition.getRow() - 1, myPosition.getColumn());
            if(board.getPiece(testPos) == null){

                if(myPosition.getRow() - 1 == 1) {
                    AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                    AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                    AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                    AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                }else{

                    AddNewMove(moves, myPosition, testPos, null);

                    testPos = new ChessPositionImple(myPosition.getRow() - 2, myPosition.getColumn());

                    //FIRST MOVE, TWO SPACES
                    if(firstMove && board.getPiece(testPos) == null ){

                        AddNewMove(moves, myPosition, testPos, null);
                    }
                }
            }

            //CHECK IF RIGHT COLUMN IS INSIDE THE BOARD
            if((myPosition.getColumn() + 1) <= 8){

                testPos = new ChessPositionImple(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                chess.ChessPiece potentialEnemy = board.getPiece(testPos);

                if (potentialEnemy != null) {
                    if(potentialEnemy.getTeamColor() == enemyColor){

                        if(myPosition.getRow() - 1 == 1) {
                            AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                            AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                            AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                            AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                        }else {
                            AddNewMove(moves, myPosition, testPos, null);
                        }
                    }
                }
            }

            //CHECK IF LEFT COLUMN US INSIDE THE BOARD
            if((myPosition.getColumn() - 1) >= 1){

                testPos = new ChessPositionImple(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                chess.ChessPiece potentialEnemy = board.getPiece(testPos);

                if (potentialEnemy != null) {
                    if(potentialEnemy.getTeamColor() == enemyColor){

                        if(myPosition.getRow() - 1 == 1) {
                            AddNewMove(moves, myPosition, testPos, PieceType.ROOK);
                            AddNewMove(moves, myPosition, testPos, PieceType.KNIGHT);
                            AddNewMove(moves, myPosition, testPos, PieceType.BISHOP);
                            AddNewMove(moves, myPosition, testPos, PieceType.QUEEN);
                        }else {
                            AddNewMove(moves, myPosition, testPos, null);
                        }
                    }
                }
            }
        }

        return moves;
    }

    private void AddNewMove(Collection<ChessMove> moves, chess.ChessPosition startPos, ChessPosition endPos, PieceType promotion){

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
