package com.chess.chess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Pawn extends Piece {

    private boolean isInInitialPosition;

    public Pawn(Color color, Position position) {
        super(color, position);
        if (position.y() == 2 || position.y() == 7) {
            isInInitialPosition = true;
        }
    }

    @Override
    public void moveTo(Position targetPosition, Board board) {
        List<Position> possibleMoves = possibleMoves(board);
        if (!possibleMoves.contains(targetPosition)) {
            throw new InvalidMoveException();
        }
        Optional<Piece> pieceAtTargetPosition = board.pieceAtPosition(targetPosition);
        boolean isThereAPieceAtTargetPosition = pieceAtTargetPosition.isPresent();
        if (isThereAPieceAtTargetPosition && (isTargetPositionInFrontOfMe(targetPosition) ||
                isTargetPositionTwoPositionsInFrontOfMe(targetPosition))) {
            throw new InvalidMoveException();
        }
        if (isThereAPieceAtTargetPosition && isTargetPositionInDiagonalToMe(targetPosition)) {
            board.removePieceAtPosition(targetPosition);
        }
        if (!isThereAPieceAtTargetPosition && isTargetPositionInDiagonalToMe(targetPosition)) {
            throw new InvalidMoveException();
        }
        setPosition(targetPosition);
        isInInitialPosition = false;
    }

    private List<Position> possibleMoves(Board board) {
        if ((position.y() >= Board.SIZE)) {
            return Collections.emptyList();
        }
        List<Position> possibleMoves = new ArrayList<>();

        Position onePositionAhead = onePositionAhead();
        Optional<Piece> pieceOnePositionAhead = board.pieceAtPosition(onePositionAhead);
        if (notInTheLastRow() && pieceOnePositionAhead.isEmpty()) {
            possibleMoves.add(onePositionAhead);
        }

        Position twoPositionAhead = twoPositionAhead();
        Optional<Piece> pieceTwoPositionAhead = board.pieceAtPosition(twoPositionAhead);
        if (isInInitialPosition && pieceOnePositionAhead.isEmpty() && pieceTwoPositionAhead.isEmpty()) {
            possibleMoves.add(twoPositionAhead);
        }
        possibleMoves.addAll(diagonalMovesIfApply(board));
        return possibleMoves;
    }

    private List<Position> diagonalMovesIfApply(Board board) {
        List<Position> positions = new ArrayList<>();
        if (position.x() == 1) {
            if (board.pieceAtPosition(diagonalToTheRight()).isPresent()) {
                positions.add(diagonalToTheRight());
            }
        }

        if (position.x() == 8) {
            if (board.pieceAtPosition(diagonalToTheLeft()).isPresent()) {
                positions.add(diagonalToTheLeft());
            }
        }

        if (position.x() != 1 && position.x() != 8) {
            if (board.pieceAtPosition(diagonalToTheLeft()).isPresent()) {
                positions.add(diagonalToTheLeft());
            }
            if (board.pieceAtPosition(diagonalToTheRight()).isPresent()) {
                positions.add(diagonalToTheRight());
            }
        }

        return positions;
    }


    private boolean isTargetPositionInDiagonalToMe(Position targetPosition) {
        int yPosition = (color == Color.BLACK) ? -1 : 1;
        Position rightDiagonalPositionFromMe = new Position(position.x() + 1, position.y() + yPosition);
        Position leftDiagonalPositionFromMe = new Position(position.x() - 1, position.y() + yPosition);
        return targetPosition.equals(rightDiagonalPositionFromMe) ||
                targetPosition.equals(leftDiagonalPositionFromMe);
    }

    private boolean isTargetPositionInFrontOfMe(Position targetPosition) {
        int yPosition = (color == Color.BLACK) ? -1 : 1;
        return position.y() + yPosition == targetPosition.y() &&
                position.x() == targetPosition.x();
    }

    private boolean isTargetPositionTwoPositionsInFrontOfMe(Position targetPosition) {
        int yPosition = (color == Color.BLACK) ? -2 : 2;
        return position.y() + yPosition == targetPosition.y() &&
                position.x() == targetPosition.x();
    }

    private Position diagonalToTheLeft() {
        int yPosition = (color == Color.BLACK) ? -1 : 1;
        return new Position(position.x() - 1, position.y() + yPosition);
    }

    private Position diagonalToTheRight() {
        int yPosition = (color == Color.BLACK) ? -1 : 1;
        return new Position(position.x() + 1, position.y() + yPosition);
    }

    private Position onePositionAhead() {
        int yPosition = (color == Color.BLACK) ? -1 : 1;
        return new Position(position.x(), position.y() + yPosition);
    }

    private Position twoPositionAhead() {
        int yPosition = (color == Color.BLACK) ? -2 : 2;
        return new Position(position.x(), position.y() + yPosition);
    }

    private boolean notInTheLastRow() {
        boolean inTheLastRow = (color == Color.WHITE && Board.SIZE == position.y());
        inTheLastRow = inTheLastRow || (color == Color.BLACK && position.y() == 1);
        return !inTheLastRow;
    }
}
