package com.game.tictactoeapi.validation;

import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameRequest.*;
import com.game.tictactoeapi.exception.InvalidMoveException;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeValidator implements GameValidator {

    @Override
    public void validate(GameRequest request) {
        var board = request.getBoard();
        var currentPlayer = request.getCurrentPlayer();
        var position = request.getPosition();

        if (board == null || board.size() != 9) {
            throw new InvalidMoveException("Invalid board state. Must be exactly 9 characters.");
        }
        if (currentPlayer == null || (currentPlayer != CurrentPlayerEnum.X && currentPlayer != CurrentPlayerEnum.O)) {
            throw new InvalidMoveException("Invalid player. Must be 'X' or 'O'.");
        }
        if (position < 0 || position > 8) {
            throw new InvalidMoveException("Out of bounds! Please choose a position between 0 and 8.");
        }
        var targetSpot = board.get(position);
        if ("X".equalsIgnoreCase(targetSpot) || "O".equalsIgnoreCase(targetSpot)) {
            throw new InvalidMoveException("Position already taken! Choose an empty spot.");
        }
    }
}
