package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameRequest.CurrentPlayerEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rule: Player Validation Specifications")
class PlayerRuleTest {

    private PlayerRule rule;
    private GameRequest request;

    @BeforeEach
    void setUp() {
        rule = new PlayerRule();
        request = new GameRequest();
    }

    @Test
    @DisplayName("Should pass when the player is X")
    void validate_shouldPass_whenPlayerIsX() {
        request.setCurrentPlayer(CurrentPlayerEnum.X);
        assertDoesNotThrow(() -> rule.validate(request));
    }

    @Test
    @DisplayName("Should pass when the player is O")
    void validate_shouldPass_whenPlayerIsO() {
        request.setCurrentPlayer(CurrentPlayerEnum.O);
        assertDoesNotThrow(() -> rule.validate(request));
    }

    @Test
    @DisplayName("Should throw exception when the current player is null")
    void validate_shouldThrowException_whenPlayerIsNull() {
        request.setCurrentPlayer(null);
        InvalidMoveException exception = assertThrows(InvalidMoveException.class, () -> rule.validate(request));
        assertEquals("Invalid player. Must be 'X' or 'O'.", exception.getMessage());
    }
}
