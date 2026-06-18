package com.game.tictactoeapi.advice;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Global Exception Handler Specifications")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private GameRequest standardRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        standardRequest = new GameRequest();
        standardRequest.setBoard(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8"));
        standardRequest.setCurrentPlayer(GameRequest.CurrentPlayerEnum.X);
        standardRequest.setPosition(4);
    }

    @Test
    @DisplayName("Should return 400 Bad Request with custom payload when InvalidMoveException is thrown")
    void handleInvalidMoveException_shouldReturn400_withCustomPayload() {
        String errorMessage = "Position already taken! Choose an empty spot.";
        TicTacToeException exception = TicTacToeException.builder()
                .message(errorMessage)
                .request(standardRequest)
                .build();
        ResponseEntity<GameResponse> responseEntity = exceptionHandler.handleInvalidMoveException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "HTTP Status should be 400 BAD REQUEST");
        GameResponse body = responseEntity.getBody();
        assertNotNull(body, "Error response body should not be null");
        assertEquals(standardRequest.getBoard(), body.getBoard(), "Original board should be returned in error response");
        assertEquals(GameResponse.NextPlayerEnum.X, body.getNextPlayer(), "Next player should default back to the player who attempted the invalid move");
        assertEquals(GameResponse.StatusEnum.INVALID_MOVE, body.getStatus(), "Status should explicitly indicate an invalid move");
        assertEquals(errorMessage, body.getMessage(), "The exception message should be passed to the user");
    }

    @Test
    @DisplayName("Should gracefully handle fallback player mapping when currentPlayer is null in bad requests")
    void handleInvalidMoveException_shouldHandleNullPlayerInFallback() {
        standardRequest.setCurrentPlayer(null);
        TicTacToeException exception = TicTacToeException.builder()
                .message("Invalid player.")
                .request(standardRequest)
                .build();
        ResponseEntity<GameResponse> responseEntity = exceptionHandler.handleInvalidMoveException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GameResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(GameResponse.NextPlayerEnum.MINUS, body.getNextPlayer(), "Null player should safely fallback to the MINUS enum");
        assertEquals("Invalid player.", body.getMessage());
    }

    @Test
    @DisplayName("Should fallback gracefully if the exception is thrown without the GameRequest context")
    void handleInvalidMoveException_shouldHandleMissingRequestContext() {
        TicTacToeException exception = TicTacToeException.builder()
                .message("Unknown error.")
                .build();
        ResponseEntity<GameResponse> responseEntity = exceptionHandler.handleInvalidMoveException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(GameResponse.NextPlayerEnum.MINUS, responseEntity.getBody().getNextPlayer());
        assertEquals("Unknown error.", responseEntity.getBody().getMessage());
    }
}