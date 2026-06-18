package com.game.tictactoeapi.controller;

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
    @DisplayName("initGame: Should return 200 OK and pass the requested size to the service")
    void initGame_shouldReturn200Ok_withInitialState() {
        GameResponse initialResponse = new GameResponse()
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Game initialized with a 4x4 board. Player X to move first.");
        when(gameService.initializeGame(4)).thenReturn(initialResponse);
        ResponseEntity<GameResponse> responseEntity = controller.initGame(4);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HTTP Status should be 200 OK");
        assertNotNull(responseEntity.getBody(), "Response body should not be null");
        assertEquals("Game initialized with a 4x4 board. Player X to move first.", responseEntity.getBody().getMessage());
        verify(gameService, times(1)).initializeGame(4);
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
}