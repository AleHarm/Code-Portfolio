package chess;

import java.util.Objects;

public class ChessPositionImple implements ChessPosition {

    private int row;
    private int col;

    public ChessPositionImple(Integer row, Integer col) {

        this.row = row;
        this.col = col;
    }

    public ChessPositionImple(){}

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImple that = (ChessPositionImple) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
