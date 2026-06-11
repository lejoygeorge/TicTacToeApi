package com.game.tictactoeapi.validation;

import com.game.tictactoeapi.model.GameRequest;

public interface GameValidator {
    void validate(GameRequest request);
}
