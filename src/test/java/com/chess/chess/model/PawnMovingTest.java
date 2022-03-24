package com.chess.chess.model;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PawnMovingTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("capturingMovesProvider")
    public void capturingMoves(String test, Pawn pawn, Board board, Position targetPosition) {
        //given
        Piece pieceToBeCaptured = board.pieceAtPosition(targetPosition).orElseThrow();

        //when
        board.move(pawn, targetPosition);

        //then
        assertThat(pawn.getPosition()).isEqualTo(targetPosition);
        assertThat(board.isPieceOnBoard(pieceToBeCaptured)).isFalse();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validMovesProvider")
    public void validMoves(String test, Pawn pawn, Board board, Position targetPosition) {
        //when
        board.move(pawn, targetPosition);

        //then
        assertThat(pawn.getPosition()).isEqualTo(targetPosition);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidMovesProvider")
    public void invalidMoves(String test, Pawn pawn, Board board, Position targetDiagonalPosition,
                             Class<? extends Runnable> exception) {
        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
                board.move(pawn, targetDiagonalPosition);

        //then
        assertThatThrownBy(throwingCallable).
                isInstanceOf(exception).
                hasMessage(null);
    }

    private static Stream<Arguments> invalidMovesProvider() {
        return Stream.of(
                whitePawnMovingForwardOnePositionToANonEmptyPosition(),
                blackPawnMovingForwardOnePositionToANonEmptyPosition(),
                whitePawnMovingForwardTwoPositionToANonEmptyPosition(),
                blackPawnMovingForwardTwoPositionToANonEmptyPosition(),
                whitePawnLocatedAtLastLevel(),
                blackPawnLocatedAtLastLevel(),
                movingToTheCurrentPosition(),
                whitePawnTryingToMoveDiagonallyToTheLeftToAVacantPosition(),
                whitePawnTryingToMoveDiagonallyToTheRightToAVacantPosition(),
                blackPawnTryingToMoveDiagonallyToTheLeftToAVacantPosition(),
                blackPawnTryingToMoveDiagonallyToTheRightToAVacantPosition(),
                outOfBorderMoveToTheBottomLimit(),
                outOfBorderMoveToTheTopLimit(),
                outOfBorderMoveToTheRightLimit(),
                outOfBorderMoveToTheLeftLimit(),
                movingTwoPositionsInvalidMove()
        );
    }

    private static Stream<Arguments> capturingMovesProvider() {
        return Stream.of(
                whitePawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight(),
                whitePawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft(),
                whitePawnCapturingBlackPieceDiagonallyToTheRight(),
                whitePawnCapturingBlackPieceDiagonallyToTheLeft(),
                blackPawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight(),
                blackPawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft(),
                blackPawnCapturingWhitePieceDiagonallyToTheRight(),
                blackPawnCapturingWhitePieceDiagonallyToTheLeft()
        );
    }

    private static Stream<Arguments> validMovesProvider() {
        return Stream.of(
                whitePawnMovingOnePositionForward(),
                blackPawnMovingOnePositionForward(),
                whitePawnMovingTwoPositionsForward(),
                blackPawnMovingTwoPositionsForward()
        );
    }

    private static Arguments blackPawnMovingOnePositionForward() {
        Position position = new Position(2,7);
        Position targetPosition = new Position(2,6);
        Pawn pawn = new Pawn(Color.BLACK, position);
        Board board = new Board(List.of(pawn));
        return Arguments.of("blackPawnMovingOnePositionForward", pawn, board, targetPosition);
    }

    private static Arguments blackPawnMovingTwoPositionsForward() {
        Position position = new Position(2,7);
        Position targetPosition = new Position(2,5);
        Pawn pawn = new Pawn(Color.BLACK, position);
        Board board = new Board(List.of(pawn));
        return Arguments.of("blackPawnMovingTwoPositionsForward", pawn, board, targetPosition);
    }

    private static Arguments movingTwoPositionsInvalidMove() {
        Position position = new Position(1, 3);
        Pawn pawn = new Pawn(Color.WHITE, position);
        Board board = new Board(List.of(pawn));
        return Arguments.of("movingTwoPositionsInvalidMove", pawn, board, new Position(1,5),
                InvalidMoveException.class);
    }

    private static Arguments outOfBorderMoveToTheBottomLimit() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4, 2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("outOfBorderMoveToTheBottomLimit", pawn, board, PositionGenerator.positionOutOfBottomLimit(),
                InvalidPositionException.class);
    }

    private static Arguments outOfBorderMoveToTheLeftLimit() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4, 2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("outOfBorderMoveToTheLeftLimit", pawn, board, PositionGenerator.positionOutOfLeftLimit(),
                InvalidPositionException.class);
    }

    private static Arguments outOfBorderMoveToTheRightLimit() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4, 2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("outOfBorderMoveToTheRightLimit", pawn, board, PositionGenerator.positionOutOfRightLimit(),
                InvalidPositionException.class);
    }

    private static Arguments outOfBorderMoveToTheTopLimit() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4, 2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("outOfBorderMoveToTheTopLimit", pawn, board, PositionGenerator.positionOutOfTopLimit(),
                InvalidPositionException.class);
    }

    private static Arguments whitePawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(8,2));
        Position positionInDiagonal = new Position(7,3);
        Pawn pawnDiagonallyLocated = new Pawn(Color.BLACK, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("whitePawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft", pawn, board, positionInDiagonal);
    }

    private static Arguments whitePawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(1,2));
        Position positionInDiagonal = new Position(2,3);
        Pawn pawnDiagonallyLocated = new Pawn(Color.BLACK, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("whitePawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight", pawn, board, positionInDiagonal);
    }

    private static Arguments whitePawnCapturingBlackPieceDiagonallyToTheRight() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4,2));
        Position positionInDiagonal = new Position(5,3);
        Pawn pawnDiagonallyLocated = new Pawn(Color.BLACK, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("whitePawnCapturingBlackPieceDiagonallyToTheRight", pawn, board, positionInDiagonal);
    }

    private static Arguments blackPawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(1,7));
        Position positionInDiagonal = new Position(2,6);
        Pawn pawnDiagonallyLocated = new Pawn(Color.WHITE, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("blackPawnAtRightLimitCapturingBlackPieceDiagonallyToTheLeft", pawn, board, positionInDiagonal);
    }

    private static Arguments blackPawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(8,7));
        Position positionInDiagonal = new Position(7,6);
        Pawn pawnDiagonallyLocated = new Pawn(Color.WHITE, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("blackPawnAtLeftLimitCapturingBlackPieceDiagonallyToTheRight", pawn, board, positionInDiagonal);
    }

    private static Arguments blackPawnCapturingWhitePieceDiagonallyToTheRight() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(4,7));
        Position positionInDiagonal = new Position(3,6);
        Pawn pawnDiagonallyLocated = new Pawn(Color.WHITE, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("blackPawnCapturingWhitePieceDiagonallyToTheRight", pawn, board, positionInDiagonal);
    }

    private static Arguments whitePawnCapturingBlackPieceDiagonallyToTheLeft() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4,2));
        Position positionInDiagonal = new Position(3,3);
        Pawn pawnDiagonallyLocated = new Pawn(Color.BLACK, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("whitePawnCapturingBlackPieceDiagonallyToTheLeft", pawn, board, positionInDiagonal);
    }

    private static Arguments blackPawnCapturingWhitePieceDiagonallyToTheLeft() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(5,7));
        Position positionInDiagonal = new Position(6,6);
        Pawn pawnDiagonallyLocated = new Pawn(Color.WHITE, positionInDiagonal);
        Board board = new Board(List.of(pawnDiagonallyLocated, pawn));
        return Arguments.of("blackPawnCapturingWhitePieceDiagonallyToTheLeft", pawn, board, positionInDiagonal);
    }

    private static Arguments blackPawnTryingToMoveDiagonallyToTheRightToAVacantPosition() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(5,7));
        Board board = new Board(List.of(pawn));
        return Arguments.of("blackPawnTryingToMoveDiagonallyToTheRightToAVacantPosition", pawn, board, new Position(4,6),
                InvalidMoveException.class);
    }

    private static Arguments whitePawnTryingToMoveDiagonallyToTheRightToAVacantPosition() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(5,2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("whitePawnTryingToMoveDiagonallyToTheRightToAVacantPosition", pawn, board, new Position(6,3),
                InvalidMoveException.class);
    }

    private static Arguments blackPawnTryingToMoveDiagonallyToTheLeftToAVacantPosition() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(5,7));
        Board board = new Board(List.of(pawn));
        return Arguments.of("blackPawnTryingToMoveDiagonallyToTheLeftToAVacantPosition", pawn, board, new Position(6,6),
                InvalidMoveException.class);
    }

    private static Arguments whitePawnTryingToMoveDiagonallyToTheLeftToAVacantPosition() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(5,2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("whitePawnTryingToMoveDiagonallyToTheLeftToAVacantPosition", pawn, board, new Position(4,3),
                InvalidMoveException.class);
    }

    private static Arguments blackPawnMovingForwardOnePositionToANonEmptyPosition() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(5,7));
        Position inFrontOfMePosition = new Position(5,6);
        Pawn pawnInFrontOfMe = new Pawn(Color.WHITE, inFrontOfMePosition);
        Board board = new Board(List.of(pawnInFrontOfMe, pawn));
        return Arguments.of("blackPawnMovingForwardOnePositionToANonEmptyPosition", pawn, board, inFrontOfMePosition,
                InvalidMoveException.class);
    }

    private static Arguments whitePawnMovingForwardOnePositionToANonEmptyPosition() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4,2));
        Position inFrontOfMePosition = new Position(4,3);
        Pawn pawnInFrontOfMe = new Pawn(Color.BLACK, inFrontOfMePosition);
        Board board = new Board(List.of(pawnInFrontOfMe, pawn));
        return Arguments.of("aPieceInFrontOfMe", pawn, board, inFrontOfMePosition, InvalidMoveException.class);
    }

    private static Arguments blackPawnMovingForwardTwoPositionToANonEmptyPosition() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(2,7));
        Position inFrontOfMePosition = new Position(2,5);
        Pawn pawnInFrontOfMe = new Pawn(Color.WHITE, inFrontOfMePosition);
        Board board = new Board(List.of(pawnInFrontOfMe, pawn));
        return Arguments.of("blackPawnMovingForwardTwoPositionToANonEmptyPosition", pawn, board, inFrontOfMePosition,
                InvalidMoveException.class);
    }

    private static Arguments whitePawnMovingForwardTwoPositionToANonEmptyPosition() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4,2));
        Position inFrontOfMePosition = new Position(4,4);
        Pawn pawnInFrontOfMe = new Pawn(Color.BLACK, inFrontOfMePosition);
        Board board = new Board(List.of(pawnInFrontOfMe, pawn));
        return Arguments.of("aPieceTwoPositionInFrontOfMe", pawn, board, inFrontOfMePosition,
                InvalidMoveException.class);
    }

    private static Arguments blackPawnLocatedAtLastLevel() {
        Pawn pawn = new Pawn(Color.BLACK, new Position(4,1));
        Position inFrontOfMePosition = new Position(4,0);
        Board board = new Board(List.of(pawn));
        return Arguments.of("blackPawnLocatedAtLastLevel", pawn, board, inFrontOfMePosition,
                InvalidPositionException.class);
    }

    private static Arguments whitePawnLocatedAtLastLevel() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(4,8));
        Position inFrontOfMePosition = new Position(4,9);
        Board board = new Board(List.of(pawn));
        return Arguments.of("whitePawnLocatedAtLastLevel", pawn, board, inFrontOfMePosition,
                InvalidPositionException.class);
    }

    private static Arguments whitePawnMovingOnePositionForward() {
        Position position = new Position(2,2);
        Position targetPosition = new Position(2,3);
        Pawn pawn = new Pawn(Color.WHITE, position);
        Board board = new Board(List.of(pawn));
        return Arguments.of("movingOnePositionForward", pawn, board, targetPosition);
    }

    private static Arguments whitePawnMovingTwoPositionsForward() {
        Position position = new Position(2,2);
        Position targetPosition = new Position(2,4);
        Pawn pawn = new Pawn(Color.WHITE, position);
        Board board = new Board(List.of(pawn));
        return Arguments.of("movingTwoPositionsForward", pawn, board, targetPosition);
    }

    private static Arguments movingToTheCurrentPosition() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(3, 2));
        Board board = new Board(List.of(pawn));
        return Arguments.of("movingToTheCurrentPosition", pawn, board, new Position(3,2),
                InvalidMoveException.class);
    }
}
