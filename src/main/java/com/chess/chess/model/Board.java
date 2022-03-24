package com.chess.chess.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

    public static final int SIZE = 8;
    private final List<Piece> pieces;

    public Board(List<Piece> pieces) {
        this.pieces = new ArrayList<>(pieces);
    }

    public void move(Piece piece, Position targetPosition) {
        validatePosition(targetPosition);
        piece.moveTo(targetPosition, this);
    }

    public Optional<Piece> pieceAtPosition(Position targetPosition) {
        validatePosition(targetPosition);
        return pieces.stream().
                filter(piece -> piece.getPosition().equals(targetPosition)).
                findFirst();
    }

    public void removePieceAtPosition(Position position) {
        validatePosition(position);
        Piece pieceToRemove = pieces.stream().
                filter(piece -> piece.getPosition().equals(position)).
                findFirst().orElseThrow();
        pieces.remove(pieceToRemove);
    }

    public boolean isPieceOnBoard(Piece piece) {
        return pieces.contains(piece);
    }

    private void validatePosition(Position targetPosition) {
        boolean withinLimit = targetPosition.x() >= 1 && targetPosition.x() <= SIZE;
        withinLimit = withinLimit && targetPosition.y() >= 1 && targetPosition.y() <= SIZE;
        if (!withinLimit) {
            throw new InvalidPositionException();
        }
    }
}
