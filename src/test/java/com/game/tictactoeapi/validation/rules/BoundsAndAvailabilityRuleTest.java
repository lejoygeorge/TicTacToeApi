package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rule: Bounds and Availability Specifications")
class BoundsAndAvailabilityRuleTest {

    private BoundsAndAvailabilityRule rule;
    private GameRequest validRequest;

    @BeforeEach
    void setUp() {
        rule = new BoundsAndAvailabilityRule();
        validRequest = new GameRequest();
        validRequest.setBoard(new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8")));
        validRequest.setPosition(4);
    }

    @Test
    @DisplayName("Should pass when the requested position is within bounds and available")
    void validate_shouldPass_whenPositionIsValid() {
        assertDoesNotThrow(() -> rule.validate(validRequest));
    }

    @ParameterizedTest(name = "Should pass when the position is at boundary {0}")
    @ValueSource(ints = {0, 8})
    @DisplayName("Boundary Validation: Positions 0 and 8 should pass for 3x3 board")
    void validate_shouldPass_whenPositionIsAtBoundary(int boundaryPosition) {
        validRequest.setPosition(boundaryPosition);
        assertDoesNotThrow(() -> rule.validate(validRequest));
    }

    @Test
    @DisplayName("Should throw exception when the position is a negative number")
    void validate_shouldThrowException_whenPositionIsNegative() {
        validRequest.setPosition(-1);

        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(validRequest));
        assertEquals("Out of bounds! Please choose a position between 0 and 8.", exception.getMessage());
    }

    @Test
    @DisplayName("Should dynamically format exception message when position is out of bounds for a 4x4 grid")
    void validate_shouldThrowException_withDynamicBounds_whenPositionIsTooHigh() {
        validRequest.setBoard(Arrays.asList(new String[16]));
        validRequest.setPosition(16);

        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(validRequest));
        assertEquals("Out of bounds! Please choose a position between 0 and 15.", exception.getMessage());
    }

    @ParameterizedTest(name = "Should throw exception when spot is already taken by ''{0}''")
    @ValueSource(strings = {"X", "O", "x", "o"})
    @DisplayName("Target Availability: Checking all player character variations")
    void validate_shouldThrowException_whenPositionIsTaken(String takenToken) {
        validRequest.getBoard().set(4, takenToken);
        validRequest.setPosition(4);

        TicTacToeException exception = assertThrows(TicTacToeException.class, () -> rule.validate(validRequest));
        assertEquals("Position already taken! Choose an empty spot.", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle null board elements safely due to Yoda conditions")
    void validate_shouldHandleNullBoardElementsSafely() {
        List<String> boardWithNull = new ArrayList<>(Arrays.asList("0", "1", "2", "3", null, "5", "6", "7", "8"));
        validRequest.setBoard(boardWithNull);
        validRequest.setPosition(4);

        assertDoesNotThrow(() -> rule.validate(validRequest));
    }
}
