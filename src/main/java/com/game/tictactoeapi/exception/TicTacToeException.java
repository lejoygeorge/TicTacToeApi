package com.game.tictactoeapi.exception;

import com.game.tictactoeapi.model.GameRequest;

public class TicTacToeException extends RuntimeException {

    private final transient GameRequest gameRequest;

    private TicTacToeException(String message, GameRequest gameRequest) {
        super(message);
        this.gameRequest = gameRequest;
    }

    public GameRequest getGameRequest() {
        return gameRequest;
    }

    public static ExceptionBuilder builder() {
        return new ExceptionBuilder();
    }

    public static class ExceptionBuilder {
        private String message;
        private GameRequest request;

        public ExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ExceptionBuilder request(GameRequest request) {
            this.request = request;
            return this;
        }

        public TicTacToeException build() {
            return new TicTacToeException(this.message, this.request);
        }
    }
}