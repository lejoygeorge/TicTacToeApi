package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.model.GameRequest;

public interface GameValidationRule {
    void validate(GameRequest request);
}