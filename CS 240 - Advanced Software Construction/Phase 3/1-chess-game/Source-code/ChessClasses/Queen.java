package ChessClasses;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Queen extends ChessPieceImple{
    private PieceType type;

    public Queen(chess.ChessGame.TeamColor teamColor) {
        super(teamColor);
        type = PieceType.QUEEN;
    }

    @Override
    public PieceType getPieceType() {
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
        //VERTICAL
        //

        int i = 1;
        while((myPosition.getRow() + i) <= 8) {

            testPos = new ChessPositionImple((myPosition.getRow() + i), myPosition.getColumn());

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
        //OTHER VERTICAL
        //

        i = 1;
        while((myPosition.getRow() - i) >= 1){

            testPos = new ChessPositionImple((myPosition.getRow() - i), myPosition.getColumn());

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
        //HORIZONTAL
        //

        i = 1;
        while((myPosition.getColumn() + i) <= 8){

            testPos = new ChessPositionImple(myPosition.getRow(), (myPosition.getColumn() + i));

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
        //OTHER HORIZONTAL
        //

        i = 1;
        while((myPosition.getColumn() - i) >= 1){

            testPos = new ChessPositionImple(myPosition.getRow(), (myPosition.getColumn() - i));

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
        //++
        //

        i = 1;
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

    private void AddNewMove(Collection<ChessMove> moves, chess.ChessPosition startPos, ChessPosition endPos, PieceType promotion){

        ChessMoveImple move = new ChessMoveImple(startPos, endPos, promotion);
        moves.add(move);
    }
}
