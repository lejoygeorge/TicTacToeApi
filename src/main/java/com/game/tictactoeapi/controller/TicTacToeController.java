package com.game.tictactoeapi.controller;

import com.game.tictactoeapi.api.GameApi;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicTacToeController implements GameApi {

    private final GameService gameService;

    @Override
    public ResponseEntity<GameResponse> initGame() {
        GameResponse response = gameService.initializeGame();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GameResponse> playMove(GameRequest gameRequest) {
        try {
            GameResponse response = gameService.playMove(gameRequest);
            return ResponseEntity.ok(response);
        } catch (InvalidMoveException ex) {
            GameResponse errorResponse = new GameResponse();
            errorResponse.setBoard(gameRequest.getBoard());
            String fallbackPlayer = gameRequest.getCurrentPlayer() != null ? gameRequest.getCurrentPlayer().getValue() : "-";
            errorResponse.setNextPlayer(GameResponse.NextPlayerEnum.fromValue(fallbackPlayer));
            errorResponse.setStatus(GameResponse.StatusEnum.INVALID_MOVE);
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
