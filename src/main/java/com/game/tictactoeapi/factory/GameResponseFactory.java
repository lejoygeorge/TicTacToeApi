package com.game.tictactoeapi.factory;

import com.game.tictactoeapi.model.GameResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameResponseFactory {

    public GameResponse create(List<String> board,
                               GameResponse.StatusEnum status,
                               GameResponse.NextPlayerEnum nextPlayer,
                               String message) {
        return new GameResponse()
                .board(board)
                .status(status)
                .nextPlayer(nextPlayer)
                .message(message);
    }
}