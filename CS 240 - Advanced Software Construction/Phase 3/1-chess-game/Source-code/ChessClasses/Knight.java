package ChessClasses;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Knight extends ChessPieceImple{
    private chess.ChessPiece.PieceType type;

    public Knight(chess.ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = chess.ChessPiece.PieceType.KNIGHT;
    }

    @Override
    public chess.ChessPiece.PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<chess.ChessMove> pieceMoves(ChessBoard board, chess.ChessPosition myPosition) {

        chess.ChessPiece piece= board.getPiece(myPosition);
        chess.ChessGame.TeamColor color = piece.getTeamColor();
        chess.ChessGame.TeamColor enemyColor;
        if(color == chess.ChessGame.TeamColor.WHITE){
            enemyColor = chess.ChessGame.TeamColor.BLACK;
        }else{
            enemyColor = ChessGame.TeamColor.WHITE;
        }
        chess.ChessPosition testPos;
        Collection<chess.ChessMove> moves = new HashSet<>();

        //
        //VERTICAL SIDE
        //


        if((myPosition.getRow() + 2) <= 8 && (myPosition.getColumn() + 1) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + 2), (myPosition.getColumn() + 1));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //VERTICAL OTHER SIDE
        //

        if((myPosition.getRow() + 2) <= 8 && (myPosition.getColumn() - 1) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() + 2), (myPosition.getColumn() - 1));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //OTHER VERTICAL SIDE
        //

        if((myPosition.getRow() - 2) >= 1 && (myPosition.getColumn() + 1) <= 8){

            testPos = new ChessPositionImple((myPosition.getRow() - 2), (myPosition.getColumn() + 1));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //OTHER VERTICAL OTHER SIDE
        //

        if((myPosition.getRow() - 2) >= 1 && (myPosition.getColumn() - 1) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - 2), (myPosition.getColumn() - 1));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //HORIZONTAL VERTICAL
        //

        if((myPosition.getRow() + 1) <= 8 && (myPosition.getColumn() + 2) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + 1), (myPosition.getColumn() + 2));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //HORIZONTAL OTHER VERTICAL
        //

        if((myPosition.getRow() - 1) >= 1 && (myPosition.getColumn() + 2) <= 8){

            testPos = new ChessPositionImple((myPosition.getRow() - 1), (myPosition.getColumn() + 2));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //OTHER HORIZONTAL VERTICAL
        //

        if((myPosition.getRow() + 1) <= 8 && (myPosition.getColumn() - 2) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() + 1), (myPosition.getColumn() - 2));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //OTHER HORIZONTAL OTHER VERTICAL
        //

        if((myPosition.getRow() - 1) >= 1 && (myPosition.getColumn() - 2) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - 1), (myPosition.getColumn() - 2));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        return moves;
    }

    private void AddNewMove(Collection<ChessMove> moves, chess.ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType promotion) {

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
