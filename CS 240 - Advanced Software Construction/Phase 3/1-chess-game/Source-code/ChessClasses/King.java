package ChessClasses;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class King extends ChessPieceImple{

    private PieceType type;

    public King(chess.ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.KING;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<chess.ChessMove> pieceMoves(ChessBoard board, chess.ChessPosition myPosition) {

        int i = 1;
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

    private void AddNewMove(Collection<ChessMove> moves, chess.ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType promotion) {

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
