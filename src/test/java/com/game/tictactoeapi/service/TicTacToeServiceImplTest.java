package com.game.tictactoeapi.service;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.ruleengine.GameRuleEngine;
import com.game.tictactoeapi.validation.GameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tic-Tac-Toe Service Orchestration Specifications")
class TicTacToeServiceImplTest {

    @Mock
    private GameValidator validator;

    @Mock
    private GameRuleEngine ruleEngine;

    @InjectMocks
    private TicTacToeServiceImpl gameService;

    private GameRequest standardRequest;

    @BeforeEach
    void setUp() {
        standardRequest = new GameRequest();
        standardRequest.setBoard(new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8")));
        standardRequest.setCurrentPlayer(GameRequest.CurrentPlayerEnum.X);
        standardRequest.setPosition(4);
    }

    @Test
    @DisplayName("initializeGame: Should return a 3x3 board when size 3 is requested")
    void initializeGame_shouldReturn3x3State_whenSizeIs3() {
        GameResponse response = gameService.initializeGame(3);
        assertNotNull(response);
        assertEquals(9, response.getBoard().size(), "3x3 board should have 9 spots");
        assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8"), response.getBoard());
        assertEquals(GameResponse.NextPlayerEnum.X, response.getNextPlayer());
        assertEquals(GameResponse.StatusEnum.IN_PROGRESS, response.getStatus());
        assertEquals("Game initialized with a 3x3 board. Player X to move first.", response.getMessage());
    }

    @Test
    @DisplayName("initializeGame: Should default to a 3x3 board if the requested size is null")
    void initializeGame_shouldFallbackTo3x3_whenSizeIsNull() {
        GameResponse response = gameService.initializeGame(null);
        assertNotNull(response);
        assertEquals(9, response.getBoard().size());
        assertEquals("Game initialized with a 3x3 board. Player X to move first.", response.getMessage());
    }


    @Test
    @DisplayName("initializeGame: Should dynamically generate a 4x4 board when size 4 is requested")
    void initializeGame_shouldReturn4x4State_whenSizeIs4() {
        GameResponse response = gameService.initializeGame(4);
        assertNotNull(response);
        assertEquals(16, response.getBoard().size(), "4x4 board should have 16 spots");
        assertEquals("15", response.getBoard().get(15), "Last spot should be 15");
        assertEquals("Game initialized with a 4x4 board. Player X to move first.", response.getMessage());
    }

    @Test
    @DisplayName("initializeGame: Should default to a 3x3 board if the requested size is less than 3")
    void initializeGame_shouldFallbackTo3x3_whenSizeIsLessThan3() {
        GameResponse response = gameService.initializeGame(2);
        assertNotNull(response);
        assertEquals(9, response.getBoard().size(), "Board size should fall back to 9 spots (3x3)");
        assertEquals("Game initialized with a 3x3 board. Player X to move first.", response.getMessage());
    }

    @Test
    @DisplayName("playMove: Should call the validator first and throw exception if invalid")
    void playMove_shouldThrowException_whenValidatorFails() {
        doThrow(new InvalidMoveException("Validation failed"))
                .when(validator).validate(standardRequest);
        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> gameService.playMove(standardRequest));
        assertEquals("Validation failed", exception.getMessage());
        verify(ruleEngine, never()).isWinner(any());
    }

    @Test
    @DisplayName("playMove: Should return GAME_OVER_WIN status when the rule engine detects a winner")
    void playMove_shouldReturnWinStatus_whenRuleEngineReturnsWinner() {
        when(ruleEngine.isWinner(any(char[].class))).thenReturn(true);
        GameResponse response = gameService.playMove(standardRequest);
        assertEquals(GameResponse.StatusEnum.GAME_OVER_WIN, response.getStatus());
        assertEquals("Player X wins the game!", response.getMessage());
        assertEquals(GameResponse.NextPlayerEnum.MINUS, response.getNextPlayer());
        assertEquals("X", response.getBoard().get(4));
    }

    @Test
    @DisplayName("playMove: Should return GAME_OVER_DRAW status when the board is full and no one won")
    void playMove_shouldReturnDrawStatus_whenRuleEngineReturnsDraw() {
        when(ruleEngine.isWinner(any(char[].class))).thenReturn(false);
        when(ruleEngine.isDraw(any(char[].class))).thenReturn(true);
        GameResponse response = gameService.playMove(standardRequest);
        assertEquals(GameResponse.StatusEnum.GAME_OVER_DRAW, response.getStatus());
        assertEquals("The game is a Draw!", response.getMessage());
        assertEquals(GameResponse.NextPlayerEnum.MINUS, response.getNextPlayer());
    }

    @Test
    @DisplayName("playMove: Should return IN_PROGRESS status and determine next player when game continues")
    void playMove_shouldReturnInProgressStatus_whenGameContinues() {
        when(ruleEngine.isWinner(any(char[].class))).thenReturn(false);
        when(ruleEngine.isDraw(any(char[].class))).thenReturn(false);
        when(ruleEngine.determineNextPlayer('X')).thenReturn('O');
        GameResponse response = gameService.playMove(standardRequest);
        assertEquals(GameResponse.StatusEnum.IN_PROGRESS, response.getStatus());
        assertEquals("Move accepted.", response.getMessage());
        assertEquals(GameResponse.NextPlayerEnum.O, response.getNextPlayer());
        assertEquals("X", response.getBoard().get(4));
    }
}