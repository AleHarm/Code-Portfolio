package ChessClasses;

import chess.ChessPiece;

/**
 * Represents moving a chess piece on a chessboard
 */
public interface ChessMove {
    /**
     * @return ChessPosition of starting location
     */
    ChessClasses.ChessPosition getStartPosition();

    /**
     * @return ChessPosition of ending location
     */
    ChessPosition getEndPosition();

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    ChessPiece.PieceType getPromotionPiece();
}
