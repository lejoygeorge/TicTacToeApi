package com.game.tictactoeapi.ruleengine;

public interface GameRuleEngine {
    boolean isWinner(char[] board);
    boolean isDraw(char[] board);
    char determineNextPlayer(char currentPlayer);
}
