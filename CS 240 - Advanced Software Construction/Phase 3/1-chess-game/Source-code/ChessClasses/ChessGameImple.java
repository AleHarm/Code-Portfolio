package ChessClasses;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ChessGameImple implements ChessGame{

    chess.ChessBoard board;
    TeamColor whosTurn;

    public ChessGameImple(){

        board = new ChessBoardImple();
        whosTurn = TeamColor.WHITE;
    }

    @Override
    public TeamColor getTeamTurn() {
        return whosTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {

        whosTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(chess.ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> proposedMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        for (Iterator<ChessMove> iterator = proposedMoves.iterator(); iterator.hasNext();) {

            ChessMove move = iterator.next();

            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);

            if(!isInCheck(color)){

                validMoves.add(move);
                undoMove(move, piece, capturedPiece);
            }

            undoMove(move, piece, capturedPiece);
        }

        return validMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

        chess.ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPos);
        TeamColor color;

        if(piece != null) {
            color = piece.getTeamColor();
        }else{
            throw new InvalidMoveException();
        }

        if(whosTurn != color) {
            throw new InvalidMoveException();
        }

        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPos);
        boolean validMove = false;

        for (Iterator<ChessMove> iterator = validMoves.iterator(); iterator.hasNext();) {

            ChessMove compMove = iterator.next();

            if(move.equals(compMove)){
                validMove = true;
                break;
            }
        }

        if(!validMove){

            throw new InvalidMoveException();
        }else{

            chess.ChessPosition endPos = move.getEndPosition();

            if (move.getPromotionPiece() != null) {

                switch(move.getPromotionPiece()){
                    case ChessPiece.PieceType.ROOK:
                        piece = new Rook(color);
                        break;
                    case ChessPiece.PieceType.KNIGHT:
                        piece = new Knight(color);
                        break;
                    case ChessPiece.PieceType.BISHOP:
                        piece = new Bishop(color);
                        break;
                    case ChessPiece.PieceType.QUEEN:
                        piece = new Queen(color);
                        break;
                }
            }

            ChessPiece capturedPiece = board.getPiece(endPos);
            board.addPiece(endPos, piece);
            board.addPiece(startPos, null);

            if(isInCheck(color)){

                undoMove(move, piece, capturedPiece);
                throw new InvalidMoveException();
            }
        }

        switchTurn();
    }

    private void switchTurn(){

        if(whosTurn == TeamColor.WHITE){
            whosTurn = TeamColor.BLACK;
        }else{
            whosTurn = TeamColor.WHITE;
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {

        TeamColor enemyColor;
        ChessPiece testPiece;
        chess.ChessPosition kingPosition;
        if(teamColor == TeamColor.WHITE){
            enemyColor = TeamColor.BLACK;
        }else{
            enemyColor = TeamColor.WHITE;
        }

        kingPosition = findTheKing(board, teamColor);

        if(kingPosition != null) {
            for (int i = 1; i <= 8; i++) {

                for (int j = 1; j <= 8; j++) {

                    chess.ChessPosition testPos = new ChessPositionImple(i, j);
                    testPiece = board.getPiece(testPos);

                    if (testPiece != null) {
                        if (testPiece.getTeamColor() == enemyColor) {

                            //get all moves
                            Collection<ChessMove> testMoves = testPiece.pieceMoves(board, testPos);
                            //iterate over the moves

                            for (Iterator<ChessMove> iterator = testMoves.iterator(); iterator.hasNext(); ) {

                                ChessMove testMove = iterator.next();

                                ChessPiece capturedPiece = board.getPiece(testMove.getEndPosition());
                                board.addPiece(testMove.getEndPosition(), testPiece);
                                board.addPiece(testMove.getStartPosition(), null);

                                if (board.getPiece(kingPosition).getTeamColor() != teamColor || board.getPiece(kingPosition).getPieceType() != ChessPiece.PieceType.KING) {

                                    undoMove(testMove, testPiece, capturedPiece);
                                    return true;
                                }

                                undoMove(testMove, testPiece, capturedPiece);
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private void undoMove(ChessMove move, ChessPiece movingPiece, ChessPiece capturedPiece){

        board.addPiece(move.getStartPosition(), movingPiece);

        if(capturedPiece != null){

            board.addPiece(move.getEndPosition(), capturedPiece);
        }else{
            board.addPiece(move.getEndPosition(), null);
        }
    }

    private chess.ChessPosition findTheKing(chess.ChessBoard board, TeamColor color){

        ChessPiece testPiece;

        for(int i = 1; i <= 8; i++){

            for(int j = 1; j <= 8; j++){

                chess.ChessPosition testPos = new ChessPositionImple(i, j);
                testPiece = board.getPiece(testPos);

                if(testPiece != null) {
                    if (testPiece.getTeamColor() == color && testPiece.getPieceType() == ChessPiece.PieceType.KING) {

                        return testPos;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {

        boolean isInCheck = isInCheck(teamColor);
        boolean noValidMoves = false;
        ChessPiece testPiece;


        for(int i = 1; i <= 8; i++){

            for(int j = 1; j <= 8; j++){

                chess.ChessPosition testPos = new ChessPositionImple(i, j);
                testPiece = board.getPiece(testPos);

                if(testPiece != null) {
                    if (testPiece.getTeamColor() == teamColor) {

                        Collection<ChessMove> moves = validMoves(testPos);

                        if(moves.size() == 0){

                            noValidMoves = true;
                        }
                    }
                }
            }
        }


        return isInCheck && noValidMoves;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {

        boolean isTeamTurn = whosTurn == teamColor;

        if(!isTeamTurn){
            return false;
        }

        boolean noValidMoves = false;
        ChessPiece testPiece;

        for(int i = 1; i <= 8; i++){

            for(int j = 1; j <= 8; j++){

                ChessPosition testPos = new ChessPositionImple(i, j);
                testPiece = board.getPiece(testPos);

                if(testPiece != null) {
                    if (testPiece.getTeamColor() == teamColor) {

                        Collection<ChessMove> moves = validMoves(testPos);

                        if(moves.size() == 0){

                            noValidMoves = true;
                        }
                    }
                }
            }
        }

        return noValidMoves;
    }

    @Override
    public void setBoard(chess.ChessBoard board) {

        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
