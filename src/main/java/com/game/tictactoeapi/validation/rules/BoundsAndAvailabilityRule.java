package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class BoundsAndAvailabilityRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        var board = request.getBoard();
        var position = request.getPosition();
        int totalSpots = board.size();

        if (position < 0 || position >= totalSpots) {
            throw new InvalidMoveException(
                    "Out of bounds! Please choose a position between 0 and %d.".formatted(totalSpots - 1)
            );
        }

        var targetSpot = board.get(position);
        if ("X".equalsIgnoreCase(targetSpot) || "O".equalsIgnoreCase(targetSpot)) {
            throw new InvalidMoveException("Position already taken! Choose an empty spot.");
        }
    }
}
