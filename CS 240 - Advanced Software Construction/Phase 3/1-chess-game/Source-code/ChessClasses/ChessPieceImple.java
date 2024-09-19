package ChessClasses;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public abstract class ChessPieceImple implements ChessPiece {

    private chess.ChessGame.TeamColor color;
    private PieceType type;

    public ChessPieceImple(chess.ChessGame.TeamColor teamColor){

        color = teamColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return null;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
