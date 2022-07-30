package data.dts.fields;

import data.dts.Position;
import data.dts.board.ChessBoard;
import data.dts.color.Color;
import data.dts.pieces.Piece;

public abstract class Field {
    protected final Position position;
    protected final ChessBoard chessBoard;

    public Field(Position position, ChessBoard chessBoard) {
        this.position = position;
        this.chessBoard = chessBoard;
    }

    public abstract boolean isEmpty();
    public abstract Piece getPiece();

    public boolean isAttackedByColor(Color color){
        //TODO
        return false;
    }

    public Position getPosition() {
        return position;
    }
}
