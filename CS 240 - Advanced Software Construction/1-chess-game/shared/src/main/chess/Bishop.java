package chess;

import java.util.Collection;
import java.util.HashSet;

public class Bishop extends ChessPieceImple{

    private PieceType type;

    public Bishop(ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.BISHOP;
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
        //++
        //

        int i = 1;
        while((myPosition.getRow() + i) <= 8 && (myPosition.getColumn() + i) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + i), (myPosition.getColumn() + i));

            if(board.getPiece(testPos) == null){

                AddNewMove(moves, myPosition, testPos, null);
            }else if(board.getPiece(testPos).getTeamColor() == color){

                break;
            }else if(board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
                break;
            }

            i++;
        }


        //
        //+-
        //

        i = 1;
        while((myPosition.getRow() + i) <= 8 && (myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() + i), (myPosition.getColumn() - i));

            if(board.getPiece(testPos) == null){

                AddNewMove(moves, myPosition, testPos, null);
            }else if(board.getPiece(testPos).getTeamColor() == color){

                break;
            }else if(board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
                break;
            }

            i++;
        }

        //
        //-+
        //

        i = 1;
        while((myPosition.getRow() - i) >= 1 && (myPosition.getColumn() + i) <= 8){

            testPos = new ChessPositionImple((myPosition.getRow() - i), (myPosition.getColumn() + i));

            if(board.getPiece(testPos) == null){

                AddNewMove(moves, myPosition, testPos, null);
            }else if(board.getPiece(testPos).getTeamColor() == color){

                break;
            }else if(board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
                break;
            }

            i++;
        }


        //
        //--
        //

        i = 1;
        while((myPosition.getRow() - i) >= 1 && (myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - i), (myPosition.getColumn() - i));

            if(board.getPiece(testPos) == null){

                AddNewMove(moves, myPosition, testPos, null);
            }else if(board.getPiece(testPos).getTeamColor() == color){

                break;
            }else if(board.getPiece(testPos).getTeamColor() == enemyColor){

                AddNewMove(moves, myPosition, testPos, null);
                break;
            }

            i++;
        }

        return moves;
    }

    private void AddNewMove(Collection<ChessMove> moves, ChessPosition startPos, ChessPosition endPos, PieceType promotion){

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
