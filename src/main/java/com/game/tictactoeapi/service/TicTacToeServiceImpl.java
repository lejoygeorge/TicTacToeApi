package com.game.tictactoeapi.service;

import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;

import com.game.tictactoeapi.ruleengine.GameRuleEngine;
import com.game.tictactoeapi.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;import java.util.stream.IntStream;

import static com.game.tictactoeapi.constants.TicTacToeConstants.DEFAULTBOARDSIZE;
import static com.game.tictactoeapi.constants.TicTacToeConstants.MININDEX;

@Service
@RequiredArgsConstructor
public class TicTacToeServiceImpl implements GameService {

    private final GameValidator validator;
    private final GameRuleEngine ruleEngine;

    @Override
    public GameResponse initializeGame(Integer size) {
        int boardSize = (size != null && size >= DEFAULTBOARDSIZE) ? size : DEFAULTBOARDSIZE;
        int totalSpots = boardSize * boardSize;

        List<String> initialBoard = IntStream.range(MININDEX, totalSpots)
                .mapToObj(String::valueOf)
                .toList();

        return new GameResponse()
                .board(initialBoard)
                .nextPlayer(GameResponse.NextPlayerEnum.X)
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Game initialized with a %dx%d board. Player X to move first.".formatted(boardSize, boardSize));
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
        char nextChar = ruleEngine.determineNextPlayer(currentPlayer.charAt(MININDEX));
        var nextPlayerEnum = GameResponse.NextPlayerEnum.fromValue(String.valueOf(nextChar));
        return new GameResponse()
                .board(board)
                .status(GameResponse.StatusEnum.IN_PROGRESS)
                .message("Move accepted.")
                .nextPlayer(nextPlayerEnum);
    }
}