package com.game.tictactoeapi.validation.rules;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.game.tictactoeapi.constants.TicTacToeConstants.*;

@Component
@Order(1)
public class BoardStateRule implements GameValidationRule {

    @Value("${game.error.validation.board.null}")
    private String boardNullMessage;

    @Value("${game.error.validation.board.size}")
    private String boardSizeMessage;

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getBoard())) {
            throw TicTacToeException.builder()
                    .message(boardNullMessage)
                    .request(request)
                    .build();
        }

        int totalSpots = request.getBoard().size();
        int boardSize = (int) Math.sqrt(totalSpots);

        if (boardSize * boardSize != totalSpots || totalSpots < DEFAULT_ARRAY_SIZE) {
            throw TicTacToeException.builder()
                    .message(boardSizeMessage)
                    .request(request)
                    .build();
        }
    }
}