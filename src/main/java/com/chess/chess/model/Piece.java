package com.chess.chess.model;

import java.util.Objects;

public abstract class Piece {

    protected Position position;
    protected final Color color;

    public Piece(Color color, Position position) {
        this.position = position;
        this.color = color;
    }

    public abstract void moveTo(Position targetPosition, Board board);

    protected void setPosition(Position position) {
        this.position = position;
    }

    protected Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return Objects.equals(position, piece.position) && color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color);
    }
}
