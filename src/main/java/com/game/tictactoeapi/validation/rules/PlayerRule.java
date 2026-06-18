package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;import java.util.Objects;

import static com.game.tictactoeapi.constants.TicTacToeConstants.ERR_MSG_INVALID_PLAYER;

@Component
@Order(2)
public class PlayerRule implements GameValidationRule {

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getCurrentPlayer())) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_INVALID_PLAYER)
                    .request(request)
                    .build();
        }
    }
}