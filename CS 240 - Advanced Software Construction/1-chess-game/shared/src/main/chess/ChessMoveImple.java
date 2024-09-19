package chess;

import java.util.Objects;

public class ChessMoveImple implements ChessMove {

    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionType;

    public ChessMoveImple(ChessPosition start, ChessPosition end, ChessPiece.PieceType type){

        startPosition = start;
        endPosition = end;
        promotionType = type;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {

        return promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImple that = (ChessMoveImple) o;
        return Objects.equals(startPosition, that.startPosition) && Objects.equals(endPosition, that.endPosition) && promotionType == that.promotionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionType);
    }
}
