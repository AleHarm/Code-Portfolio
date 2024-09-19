package chess;

import java.util.Collection;
import java.util.HashSet;

public class Knight extends ChessPieceImple{
    private PieceType type;

    public Knight(ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.KNIGHT;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece= board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessGame.TeamColor enemyColor;
        if(color == ChessGame.TeamColor.WHITE){
            enemyColor = ChessGame.TeamColor.BLACK;
        }else{
            enemyColor = ChessGame.TeamColor.WHITE;
        }
        ChessPosition testPos;
        Collection<ChessMove> moves = new HashSet<>();

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

    private void AddNewMove(Collection<ChessMove> moves, ChessPosition startPos, ChessPosition endPos, PieceType promotion) {

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
