package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rule: Board State Validation Specifications")
class BoardStateRuleTest {

    private BoardStateRule rule;
    private GameRequest request;

    @BeforeEach
    void setUp() {
        rule = new BoardStateRule();
        request = new GameRequest();
        ReflectionTestUtils.setField(rule, "boardNullMessage", "Invalid board state. Board cannot be null.");
        ReflectionTestUtils.setField(rule, "boardSizeMessage", "Invalid board state. Array size must be a perfect square (e.g., 9, 16).");
    }

    @Test
    @DisplayName("Should pass when the board is a valid 3x3 grid (9 spots)")
    void validate_shouldPass_whenBoardIs9Spots() {
        request.setBoard(Arrays.asList(new String[9]));
        assertDoesNotThrow(() -> rule.validate(request));
    }

    @Test
    @DisplayName("Should pass when the board is a valid dynamically sized 4x4 grid (16 spots)")
    void validate_shouldPass_whenBoardIs16Spots() {
        request.setBoard(Arrays.asList(new String[16]));
        assertDoesNotThrow(() -> rule.validate(request));
    }

    @Test
    @DisplayName("Should throw exception when the board is null")
    void validate_shouldThrowException_whenBoardIsNull() {
        request.setBoard(null);
        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(request));
        assertEquals("Invalid board state. Board cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when board size is less than 9 (e.g., a 2x2 grid)")
    void validate_shouldThrowException_whenBoardIsTooSmall() {
        request.setBoard(Arrays.asList(new String[4]));
        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(request));
        assertEquals("Invalid board state. Array size must be a perfect square (e.g., 9, 16).", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when board size is not a perfect square (e.g., 10 spots)")
    void validate_shouldThrowException_whenBoardIsNotPerfectSquare() {
        request.setBoard(Arrays.asList(new String[10]));
        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(request));
        assertEquals("Invalid board state. Array size must be a perfect square (e.g., 9, 16).", exception.getMessage());
    }
}