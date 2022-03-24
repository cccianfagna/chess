package com.chess.chess.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PositionGenerator {

    public static List<Position> allPositions() {
        List<Position> allPositions = new ArrayList<>();
        IntStream.range(1,9).forEach(i ->
                IntStream.range(1, 9).forEach(j ->
                        allPositions.add(new Position(i, j))));
        return allPositions;
    }

    public static Position positionOutOfTopLimit() {
        return new Position(4, Board.SIZE + 1);
    }

    public static Position positionOutOfLeftLimit() {
        return new Position(Board.SIZE + 1, 4);
    }

    public static Position positionOutOfRightLimit() {
        return new Position( 0, 4);
    }

    public static Position positionOutOfBottomLimit() {
        return new Position( 4, 0);
    }
}
