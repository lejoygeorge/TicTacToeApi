package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;import java.util.Objects;

@Component
@Order(2)
public class PlayerRule implements GameValidationRule {

    @Value("${game.error.validation.invalid.player}")
    private String invalidPlayerMessage;

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getCurrentPlayer())) {
            throw TicTacToeException.builder()
                    .message(invalidPlayerMessage)
                    .request(request)
                    .build();
        }
    }
}