package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;import static com.game.tictactoeapi.constants.TicTacToeConstants.DEFAULTARRAYSIZE;

@Component
@Order(1)
public class BoardStateRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getBoard())) {
            throw new InvalidMoveException("Invalid board state. Board cannot be null.", request);
        }
        int totalSpots = request.getBoard().size();
        int boardSize = (int) Math.sqrt(totalSpots);
        if (boardSize * boardSize != totalSpots || totalSpots < DEFAULTARRAYSIZE) {
            throw new InvalidMoveException("Invalid board state. Array size must be a perfect square (e.g., 9, 16).", request);
        }
    }
}