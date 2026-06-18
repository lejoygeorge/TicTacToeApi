package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.InvalidMoveException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;import java.util.Objects;

@Component
@Order(2)
public class PlayerRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getCurrentPlayer())) {
            throw new InvalidMoveException("Invalid player. Must be 'X' or 'O'.", request);
        }
    }
}