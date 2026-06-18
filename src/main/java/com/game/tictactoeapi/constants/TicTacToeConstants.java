package com.game.tictactoeapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TicTacToeConstants {
    public static final int DEFAULT_ARRAY_SIZE = 9;
    public static final int DEFAULT_BOARD_SIZE = 3;
    public static final int MIN_INDEX = 0;

    public static final String GAME_INIT_MESSAGE = "Game initialized with a %dx%d board. Player X to move first.";
    public static final String GAME_WIN_MESSAGE = "Player %s wins the game!";
    public static final String GAME_DRAW_MESSAGE = "The game is a Draw!";
    public static final String GAME_MOVE_ACCEPTED_MESSAGE = "Move accepted.";

    public static final String ERR_MSG_NULL_BOARD_STATE = "Invalid board state. Board cannot be null.";
    public static final String ERR_MSG_NON_SQUARE_BOARD = "Invalid board state. Array size must be a perfect square (e.g., 9, 16).";

    public static final String ERR_MSG_POSITION_TAKEN = "Position already taken! Choose an empty spot.";
    public static final String ERR_MSG_POSITION_OUT_OF_BOUNDS = "Out of bounds! Please choose a position between 0 and %d.";

    public static final String ERR_MSG_INVALID_PLAYER = "Invalid player. Must be 'X' or 'O'.";
}
