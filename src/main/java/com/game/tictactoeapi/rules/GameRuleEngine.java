package com.game.tictactoeapi.rules;

public interface GameRuleEngine {
    boolean isWinner(char[] board);
    boolean isDraw(char[] board);
    char determineNextPlayer(char currentPlayer);
}
