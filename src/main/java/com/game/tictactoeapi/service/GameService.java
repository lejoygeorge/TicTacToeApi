package com.game.tictactoeapi.service;

import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;

public interface GameService {
    GameResponse initializeGame();
    GameResponse playMove(GameRequest request);
}