package com.game.tictactoeapi.service;

import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;

import com.game.tictactoeapi.rules.GameRuleEngine;
import com.game.tictactoeapi.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicTacToeServiceImpl implements GameService {

    private final GameValidator validator;
    private final GameRuleEngine ruleEngine;

    @Override
    public GameResponse initializeGame() {
        return new GameResponse()
                .board(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8"))
                .nextPlayer(GameResponse.NextPlayerEnum.X)
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Game initialized. Player X to move first.");
    }

    @Override
    public GameResponse playMove(GameRequest request) {
        validator.validate(request);
        var board = request.getBoard();
        var currentPlayer = request.getCurrentPlayer().getValue();
        var position = request.getPosition();
        board.set(position, currentPlayer);
        char[] boardArray = String.join("", board).toCharArray();
        if (ruleEngine.isWinner(boardArray)) {
            return new GameResponse()
                    .board(board)
                    .status(GameResponse.StatusEnum.GAME_OVER_WIN)
                    .message("Player %s wins the game!".formatted(currentPlayer))
                    .nextPlayer(GameResponse.NextPlayerEnum.MINUS);
        }
        if (ruleEngine.isDraw(boardArray)) {
            return new GameResponse()
                    .board(board)
                    .status(GameResponse.StatusEnum.GAME_OVER_DRAW)
                    .message("The game is a Draw!")
                    .nextPlayer(GameResponse.NextPlayerEnum.MINUS);
        }
        char nextChar = ruleEngine.determineNextPlayer(currentPlayer.charAt(0));
        var nextPlayerEnum = GameResponse.NextPlayerEnum.fromValue(String.valueOf(nextChar));
        return new GameResponse()
                .board(board)
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Move accepted.")
                .nextPlayer(nextPlayerEnum);
    }
}