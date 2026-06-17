package com.game.tictactoeapi.rules;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine {
    
    private static final int BOARD_SIZE = 3;
    
    private static final int[][] WIN_COMBINATIONS = generateWinCombinations(BOARD_SIZE);
    
    private static int[][] generateWinCombinations(int n) {
        var rows = IntStream.range(0, n)
                .mapToObj(i -> IntStream.range(0, n).map(j -> i * n + j).toArray());

        var columns = IntStream.range(0, n)
                .mapToObj(j -> IntStream.range(0, n).map(i -> i * n + j).toArray());

        var diagonals = Stream.of(
                IntStream.range(0, n).map(i -> i * n + i).toArray(),          
                IntStream.range(0, n).map(i -> i * n + (n - 1 - i)).toArray());
        
        return Stream.concat(Stream.concat(rows, columns), diagonals).toArray(int[][]::new);
    }

    @Override
    public boolean isWinner(char[] board) {
        return Arrays.stream(WIN_COMBINATIONS).anyMatch(combo -> {
            var first = board[combo[0]];
            if (first != 'X' && first != 'O') {
                return false;
            }
            return Arrays.stream(combo).allMatch(index -> board[index] == first);
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
