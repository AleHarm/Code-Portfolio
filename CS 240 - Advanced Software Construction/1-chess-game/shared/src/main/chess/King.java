package chess;

import java.util.Collection;
import java.util.HashSet;

public class King extends ChessPieceImple{

    private PieceType type;

    public King(ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.KING;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int i = 1;
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
        //VERTICAL
        //


        if((myPosition.getRow() + i) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + i), myPosition.getColumn());

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //OTHER VERTICAL
        //

        if((myPosition.getRow() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - i), myPosition.getColumn());

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //HORIZONTAL
        //

        if((myPosition.getColumn() + i) <= 8){

            testPos = new ChessPositionImple(myPosition.getRow(), (myPosition.getColumn() + i));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //OTHER HORIZONTAL
        //

        if((myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple(myPosition.getRow(), (myPosition.getColumn() - i));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //++
        //

        if((myPosition.getRow() + i) <= 8 && (myPosition.getColumn() + i) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + i), (myPosition.getColumn() + i));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //+-
        //

        if((myPosition.getRow() + i) <= 8 && (myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() + i), (myPosition.getColumn() - i));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }

        //
        //-+
        //

        if((myPosition.getRow() - i) >= 1 && (myPosition.getColumn() + i) <= 8){

            testPos = new ChessPositionImple((myPosition.getRow() - i), (myPosition.getColumn() + i));

            if(board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
            }
        }


        //
        //--
        //

        if((myPosition.getRow() - i) >= 1 && (myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - i), (myPosition.getColumn() - i));

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
