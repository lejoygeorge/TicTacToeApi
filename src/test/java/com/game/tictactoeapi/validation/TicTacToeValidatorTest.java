package com.game.tictactoeapi.validation;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.validation.rules.GameValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tic-Tac-Toe Validator Orchestration Specifications")
class TicTacToeValidatorTest {

    @Mock
    private GameValidationRule rule1;

    @Mock
    private GameValidationRule rule2;

    @Mock
    private GameValidationRule rule3;

    private GameRequest dummyRequest;

    @BeforeEach
    void setUp() {
       dummyRequest = new GameRequest();
    }

    @Test
    @DisplayName("validate: Should execute all injected rules in sequential order")
    void validate_shouldExecuteAllRulesInOrder_whenNoExceptionsAreThrown() {
        List<GameValidationRule> rules = Arrays.asList(rule1, rule2, rule3);
        TicTacToeValidator validator = new TicTacToeValidator(rules);

        assertDoesNotThrow(() -> validator.validate(dummyRequest));

        InOrder inOrder = inOrder(rule1, rule2, rule3);
        inOrder.verify(rule1).validate(dummyRequest);
        inOrder.verify(rule2).validate(dummyRequest);
        inOrder.verify(rule3).validate(dummyRequest);
    }

    @Test
    @DisplayName("validate: Should halt execution and propagate exception if a rule fails")
    void validate_shouldHaltAndThrow_whenAnyRuleFails() {
        List<GameValidationRule> rules = Arrays.asList(rule1, rule2, rule3);
        TicTacToeValidator validator = new TicTacToeValidator(rules);

        doThrow( TicTacToeException.builder()
                .message("Rule 2 violated!")
                .build()).when(rule2).validate(dummyRequest);

        TicTacToeException exception = assertThrows(TicTacToeException.class,
                () -> validator.validate(dummyRequest));

        assertEquals("Rule 2 violated!", exception.getMessage());
        verify(rule1, times(1)).validate(dummyRequest); // Rule 1 passed
        verify(rule2, times(1)).validate(dummyRequest); // Rule 2 failed
        verify(rule3, never()).validate(any());         // Rule 3 was safely skipped
    }

    @Test
    @DisplayName("validate: Should pass safely if the injected rules list is completely empty")
    void validate_shouldPass_whenRulesListIsEmpty() {
        TicTacToeValidator validator = new TicTacToeValidator(Collections.emptyList());

        assertDoesNotThrow(() -> validator.validate(dummyRequest),
                "An empty rules list should iterate safely without crashing.");
    }
}