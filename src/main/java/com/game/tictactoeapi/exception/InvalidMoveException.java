package com.game.tictactoeapi.exception;

import com.game.tictactoeapi.model.GameRequest;

public class InvalidMoveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient GameRequest gameRequest;

    public InvalidMoveException(String message) {
        super(message);
        this.gameRequest = null;
    }

    public InvalidMoveException(String message, GameRequest gameRequest) {
        super(message);
        this.gameRequest = gameRequest;
    }

    public GameRequest getGameRequest() {
        return gameRequest;
    }
}
