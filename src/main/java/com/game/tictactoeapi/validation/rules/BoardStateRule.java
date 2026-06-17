package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class BoardStateRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        var board = request.getBoard();
        if (board == null) {
            throw new InvalidMoveException("Invalid board state. Board cannot be null.");
        }

        int totalSpots = board.size();
        int n = (int) Math.sqrt(totalSpots);

        if (n * n != totalSpots || totalSpots < 9) {
            throw new InvalidMoveException("Invalid board state. Array size must be a perfect square (e.g., 9, 16).");
        }
    }
}