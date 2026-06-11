package com.game.tictactoeapi.controller;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tic-Tac-Toe Controller API Specifications")
class TicTacToeControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private TicTacToeController controller;

    private GameRequest standardRequest;
    private GameResponse successResponse;

    @BeforeEach
    void setUp() {
        standardRequest = new GameRequest()
                .board(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8"))
                .currentPlayer(GameRequest.CurrentPlayerEnum.X)
                .position(4);
        
        successResponse = new GameResponse()
                .board(List.of("0", "1", "2", "3", "X", "5", "6", "7", "8"))
                .nextPlayer(GameResponse.NextPlayerEnum.O)
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Move accepted.");
    }

    @Test
    @DisplayName("initGame: Should return 200 OK and the initial game state")
    void initGame_shouldReturn200Ok_withInitialState() {
        GameResponse initialResponse = new GameResponse()
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Game initialized.");
        when(gameService.initializeGame()).thenReturn(initialResponse);
        ResponseEntity<GameResponse> responseEntity = controller.initGame();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HTTP Status should be 200 OK");
        assertNotNull(responseEntity.getBody(), "Response body should not be null");
        assertEquals("Game initialized.", responseEntity.getBody().getMessage());
        verify(gameService, times(1)).initializeGame();
    }

    @Test
    @DisplayName("playMove: Should return 200 OK when a valid move is played")
    void playMove_shouldReturn200Ok_whenMoveIsValid() {
        when(gameService.playMove(any(GameRequest.class))).thenReturn(successResponse);
        ResponseEntity<GameResponse> responseEntity = controller.playMove(standardRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HTTP Status should be 200 OK");
        assertNotNull(responseEntity.getBody());
        assertEquals(GameResponse.StatusEnum.IN_PROGRESS, responseEntity.getBody().getStatus());
        assertEquals(GameResponse.NextPlayerEnum.O, responseEntity.getBody().getNextPlayer());
    }

    @Test
    @DisplayName("playMove: Should catch InvalidMoveException and return 400 Bad Request with custom payload")
    void playMove_shouldReturn400BadRequest_whenInvalidMoveExceptionIsThrown() {
        String errorMessage = "Position already taken! Choose an empty spot.";
        when(gameService.playMove(any(GameRequest.class))).thenThrow(new InvalidMoveException(errorMessage));
        ResponseEntity<GameResponse> responseEntity = controller.playMove(standardRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "HTTP Status should be 400 BAD REQUEST");
        GameResponse body = responseEntity.getBody();
        assertNotNull(body, "Error response body should not be null");
        assertEquals(standardRequest.getBoard(), body.getBoard(), "Original board should be returned in error response");
        assertEquals(GameResponse.NextPlayerEnum.X, body.getNextPlayer(), "Next player should default back to the player who attempted the invalid move");
        assertEquals(GameResponse.StatusEnum.INVALID_MOVE, body.getStatus(), "Status should explicitly indicate an invalid move");
        assertEquals(errorMessage, body.getMessage(), "The exception message should be passed to the user");
    }

    @Test
    @DisplayName("playMove: Should gracefully handle fallback player mapping when currentPlayer is null in bad requests")
    void playMove_shouldHandleNullPlayerInFallback_whenRequestIsInvalid() {
        standardRequest.setCurrentPlayer(null); // Simulate a bad JSON payload from the client
        when(gameService.playMove(any(GameRequest.class))).thenThrow(new InvalidMoveException("Invalid player."));
        ResponseEntity<GameResponse> responseEntity = controller.playMove(standardRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GameResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(GameResponse.NextPlayerEnum.MINUS, body.getNextPlayer(), "Null player should safely fallback to the MINUS enum");
        assertEquals("Invalid player.", body.getMessage());
    }
}