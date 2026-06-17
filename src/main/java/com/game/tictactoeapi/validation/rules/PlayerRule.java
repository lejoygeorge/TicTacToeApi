package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameRequest.CurrentPlayerEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class PlayerRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        var currentPlayer = request.getCurrentPlayer();
        if (currentPlayer == null || (currentPlayer != CurrentPlayerEnum.X && currentPlayer != CurrentPlayerEnum.O)) {
            throw new InvalidMoveException("Invalid player. Must be 'X' or 'O'.");
        }
    }
}