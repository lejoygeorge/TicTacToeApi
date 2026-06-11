package com.game.tictactoeapi.rules;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine {

    private static final int[][] WIN_COMBINATIONS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical
            {0, 4, 8}, {2, 4, 6}             // Diagonal
    };

    @Override
    public boolean isWinner(char[] board) {
        return Arrays.stream(WIN_COMBINATIONS).anyMatch(combo -> {
            var first = board[combo[0]];
            return (first == 'X' || first == 'O')
                    && first == board[combo[1]]
                    && first == board[combo[2]];
        });
    }

    @Override
    public boolean isDraw(char[] board) {
        return String.valueOf(board).matches("^[XO]+$");
    }

    @Override
    public char determineNextPlayer(char currentPlayer) {
        return currentPlayer == 'X' ? 'O' : 'X';
    }
}
