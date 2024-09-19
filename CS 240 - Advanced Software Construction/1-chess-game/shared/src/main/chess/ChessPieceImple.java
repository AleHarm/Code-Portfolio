package chess;

import java.util.Collection;

public abstract class ChessPieceImple implements ChessPiece {

    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPieceImple(ChessGame.TeamColor teamColor){

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
