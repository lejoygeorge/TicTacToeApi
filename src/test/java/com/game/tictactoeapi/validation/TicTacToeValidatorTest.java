package com.game.tictactoeapi.validation;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameRequest.CurrentPlayerEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic-Tac-Toe Validator Specifications")
class TicTacToeValidatorTest {

    private TicTacToeValidator validator;
    private GameRequest validRequest;

    @BeforeEach
    void setUp() {
        validator = new TicTacToeValidator();
        validRequest = new GameRequest();
        validRequest.setBoard(new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8")));
        validRequest.setCurrentPlayer(CurrentPlayerEnum.X);
        validRequest.setPosition(4);
    }

    @Test
    @DisplayName("validate: Should not throw any exception for a perfectly valid move")
    void validate_shouldPass_whenRequestIsValid() {
        assertDoesNotThrow(() -> validator.validate(validRequest),
                "A valid request should pass validation without throwing an exception.");
    }

    // Board Validation Tests

    @Test
    @DisplayName("validate: Should throw exception when the board is null")
    void validate_shouldThrowException_whenBoardIsNull() {
        validRequest.setBoard(null);

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Invalid board state. Must be exactly 9 characters.", exception.getMessage());
    }

    @Test
    @DisplayName("validate: Should throw exception when the board size is not exactly 9")
    void validate_shouldThrowException_whenBoardSizeIsInvalid() {
        validRequest.setBoard(Arrays.asList("0", "1", "2"));

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Invalid board state. Must be exactly 9 characters.", exception.getMessage());
    }

    // Player Validation Tests

    @Test
    @DisplayName("validate: Should throw exception when the current player is null")
    void validate_shouldThrowException_whenPlayerIsNull() {
        validRequest.setCurrentPlayer(null);

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Invalid player. Must be 'X' or 'O'.", exception.getMessage());
    }

    // Position Validation Tests

    @Test
    @DisplayName("validate: Should throw exception when the position is a negative number")
    void validate_shouldThrowException_whenPositionIsNegative() {
        validRequest.setPosition(-1);

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Out of bounds! Please choose a position between 0 and 8.", exception.getMessage());
    }

    @Test
    @DisplayName("validate: Should throw exception when the position is greater than 8")
    void validate_shouldThrowException_whenPositionIsTooHigh() {
        validRequest.setPosition(9);

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Out of bounds! Please choose a position between 0 and 8.", exception.getMessage());
    }

    // Target Spot Validation Tests

    @Test
    @DisplayName("validate: Should throw exception when the targeted spot is already taken by 'X'")
    void validate_shouldThrowException_whenPositionIsTakenByX() {
        validRequest.getBoard().set(4, "X");
        validRequest.setPosition(4);
        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Position already taken! Choose an empty spot.", exception.getMessage());
    }

    @Test
    @DisplayName("validate: Should throw exception when the targeted spot is already taken by 'O'")
    void validate_shouldThrowException_whenPositionIsTakenByO() {
        validRequest.getBoard().set(4, "O");
        validRequest.setPosition(4);
        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Position already taken! Choose an empty spot.", exception.getMessage());
    }

    @Test
    @DisplayName("validate: Should safely evaluate and throw exception even if the taken spot is lowercase 'x' or 'o'")
    void validate_shouldThrowException_whenPositionIsTakenByLowercase() {
        validRequest.getBoard().set(4, "x");
        validRequest.setPosition(4);
        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> validator.validate(validRequest));
        assertEquals("Position already taken! Choose an empty spot.", exception.getMessage());
    }

    @Test
    @DisplayName("validate: Should not throw exception if the board contains null elements, thanks to Yoda conditions")
    void validate_shouldHandleNullBoardElementsSafely() {
        List<String> boardWithNull = new ArrayList<>(Arrays.asList("0", "1", "2", "3", null, "5", "6", "7", "8"));
        validRequest.setBoard(boardWithNull);
        validRequest.setPosition(4);
        assertDoesNotThrow(() -> validator.validate(validRequest),
                "Null elements in the board array should be handled gracefully without throwing NullPointerException");
    }
}