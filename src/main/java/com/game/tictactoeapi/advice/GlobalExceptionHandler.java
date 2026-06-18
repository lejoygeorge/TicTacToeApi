package com.game.tictactoeapi.advice;

import com.game.tictactoeapi.exception.TicTacToeException;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.model.GameRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicTacToeException.class)
    public ResponseEntity<GameResponse> handleInvalidMoveException(TicTacToeException ex) {
        GameResponse errorResponse = new GameResponse();
        GameRequest request = ex.getGameRequest();
        if (request != null) {
            errorResponse.setBoard(request.getBoard());
            String fallbackPlayer = Optional.ofNullable(request.getCurrentPlayer())
                    .map(GameRequest.CurrentPlayerEnum::getValue)
                    .orElse("-");
            errorResponse.setNextPlayer(GameResponse.NextPlayerEnum.fromValue(fallbackPlayer));
        } else {
            errorResponse.setNextPlayer(GameResponse.NextPlayerEnum.MINUS);
        }
        errorResponse.setStatus(GameResponse.StatusEnum.INVALID_MOVE);
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}