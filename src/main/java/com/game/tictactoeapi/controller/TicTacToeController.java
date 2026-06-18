package com.game.tictactoeapi.controller;

import com.game.tictactoeapi.api.GameApi;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicTacToeController implements GameApi {

    private final GameService gameService;

    @Override
    public ResponseEntity<GameResponse> initGame(Integer size) {
        GameResponse response = gameService.initializeGame(size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GameResponse> playMove(@Valid GameRequest gameRequest) {
        return ResponseEntity.ok(gameService.playMove(gameRequest));
    }
}