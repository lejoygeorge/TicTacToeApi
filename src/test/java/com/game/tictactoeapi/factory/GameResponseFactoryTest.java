package com.game.tictactoeapi.factory;

import com.game.tictactoeapi.model.GameResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Game Response Factory Specifications")
class GameResponseFactoryTest {

    private GameResponseFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GameResponseFactory();
    }

    @Test
    @DisplayName("create: Should map all parameters perfectly into a new GameResponse object")
    void create_shouldMapAllFieldsCorrectly() {
        List<String> expectedBoard = Arrays.asList("X", "O", "X", "3", "4", "5", "6", "7", "8");
        GameResponse.StatusEnum expectedStatus = GameResponse.StatusEnum.IN_PROGRESS;
        GameResponse.NextPlayerEnum expectedNextPlayer = GameResponse.NextPlayerEnum.O;
        String expectedMessage = "Move accepted.";

        GameResponse actualResponse = factory.create(expectedBoard, expectedStatus, expectedNextPlayer, expectedMessage);

        assertNotNull(actualResponse, "The generated response should not be null");
        assertEquals(expectedBoard, actualResponse.getBoard(), "The board state should be mapped exactly");
        assertEquals(expectedStatus, actualResponse.getStatus(), "The game status should be mapped exactly");
        assertEquals(expectedNextPlayer, actualResponse.getNextPlayer(), "The next player should be mapped exactly");
        assertEquals(expectedMessage, actualResponse.getMessage(), "The message should be mapped exactly");
    }

    @Test
    @DisplayName("create: Should safely handle null values without throwing exceptions")
    void create_shouldHandleNullInputsSafely() {
        GameResponse actualResponse = factory.create(null, null, null, null);

        assertNotNull(actualResponse);
        assertEquals(null, actualResponse.getBoard());
        assertEquals(null, actualResponse.getStatus());
        assertEquals(null, actualResponse.getNextPlayer());
        assertEquals(null, actualResponse.getMessage());
    }
}