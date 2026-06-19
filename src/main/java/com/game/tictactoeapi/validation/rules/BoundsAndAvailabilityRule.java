package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.game.tictactoeapi.constants.TicTacToeConstants.*;

@Component
@Order(3)
public class BoundsAndAvailabilityRule implements GameValidationRule {

    @Value("${game.error.validation.position.taken}")
    private String positionTakenMessage;

    @Value("${game.error.validation.position.out.of.bound}")
    private String outOfBoundMessage;

    @Override
    public void validate(GameRequest request) {
        var board = request.getBoard();
        var position = request.getPosition();
        int totalSpots = board.size();

        if (position < MIN_INDEX || position >= totalSpots) {
            throw TicTacToeException.builder()
                    .message(outOfBoundMessage.formatted(totalSpots - 1))
                    .request(request)
                    .build();
        }

        var targetSpot = board.get(position);
        if ("X".equalsIgnoreCase(targetSpot) || "O".equalsIgnoreCase(targetSpot)) {
            throw TicTacToeException.builder()
                    .message(positionTakenMessage)
                    .request(request)
                    .build();
        }
    }
}
