package com.game.tictactoeapi.exception;

import com.game.tictactoeapi.model.GameRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("TicTacToeException Builder Specifications")
class TicTacToeExceptionTest {

    @Test
    @DisplayName("Should build exception with both message and request context")
    void shouldBuildException_withMessageAndRequest() {
        String expectedMessage = "Invalid move detected.";
        GameRequest expectedRequest = new GameRequest();
        expectedRequest.setPosition(4);

        TicTacToeException exception = TicTacToeException.builder()
                .message(expectedMessage)
                .request(expectedRequest)
                .build();

        assertEquals(expectedMessage, exception.getMessage(), "The message should be passed to the superclass");
        assertEquals(expectedRequest, exception.getGameRequest(), "The request object should be stored exactly as passed");
    }

    @Test
    @DisplayName("Should build safely when only the message is provided")
    void shouldBuildException_withMessageOnly() {
        String expectedMessage = "Unknown error occurred.";

        TicTacToeException exception = TicTacToeException.builder()
                .message(expectedMessage)
                .build();

        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getGameRequest(), "GameRequest should be null if not provided to the builder");
    }

    @Test
    @DisplayName("Should build safely when only the request is provided")
    void shouldBuildException_withRequestOnly() {
        GameRequest expectedRequest = new GameRequest();

        TicTacToeException exception = TicTacToeException.builder()
                .request(expectedRequest)
                .build();

        assertNull(exception.getMessage(), "Message should be null if not provided to the builder");
        assertEquals(expectedRequest, exception.getGameRequest());
    }

    @Test
    @DisplayName("Should build safely when no arguments are provided")
    void shouldBuildException_withNoArguments() {
        TicTacToeException exception = TicTacToeException.builder().build();

        assertNull(exception.getMessage());
        assertNull(exception.getGameRequest());
    }
}