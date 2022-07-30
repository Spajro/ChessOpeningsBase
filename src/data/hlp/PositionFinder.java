package src.data.hlp;

import src.data.dts.Position;
import src.data.dts.pieces.Piece;

import java.util.Set;

public class PositionFinder {

    public static Position findStartingPosition(Piece piece) throws Exception {
        Set<Position> possiblePositions = piece.getPossibleStartPositions();
        if (possiblePositions.size() == 0) {
            return new Position();
        } else if (possiblePositions.size() == 1) {
            return possiblePositions.stream().findFirst().get();
        } else {
            throw new Exception("two possible positions found");
        }
    }
}
