package com.game.tictactoeapi.ruleengine;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.util.regex.Pattern;

import static com.game.tictactoeapi.constants.TicTacToeConstants.MININDEX;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine {

    private static final Pattern DRAW_PATTERN = Pattern.compile("^[XO]+$");

    private final Map<Integer, int[][]> winCombinationsCache = new ConcurrentHashMap<>();

    private int[][] getWinCombinations(int boardSize) {
        return winCombinationsCache.computeIfAbsent(boardSize, this::generateWinCombinations);
    }

    private int[][] generateWinCombinations(int boardSize) {
        var rows = IntStream.range(MININDEX, boardSize)
                .mapToObj(i -> IntStream.range(MININDEX, boardSize).map(j -> i * boardSize + j).toArray());
        var columns = IntStream.range(MININDEX, boardSize)
                .mapToObj(j -> IntStream.range(MININDEX, boardSize).map(i -> i * boardSize + j).toArray());
        var diagonals = Stream.of(
                IntStream.range(MININDEX, boardSize).map(i -> i * boardSize + i).toArray(),
                IntStream.range(MININDEX, boardSize).map(i -> i * boardSize + (boardSize - 1 - i)).toArray()
        );
        return Stream.concat(Stream.concat(rows, columns), diagonals).toArray(int[][]::new);
    }

    @Override
    public boolean isWinner(char[] board) {
        int n = (int) Math.sqrt(board.length);
        int[][] combinations = getWinCombinations(n);
        return Arrays.stream(combinations).anyMatch(combo -> {
            var first = board[combo[MININDEX]];
            if (first != 'X' && first != 'O') {
                return false;
            }
            return Arrays.stream(combo).allMatch(index -> board[index] == first);
        });
    }

    @Override
    public boolean isDraw(char[] board) {
        return DRAW_PATTERN.matcher(String.valueOf(board)).matches();
    }

    @Override
    public char determineNextPlayer(char currentPlayer) {
        return currentPlayer == 'X' ? 'O' : 'X';
    }
}