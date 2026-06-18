package com.game.tictactoeapi.service;

import com.game.tictactoeapi.factory.GameResponseFactory;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.ruleengine.GameRuleEngine;
import com.game.tictactoeapi.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static com.game.tictactoeapi.constants.TicTacToeConstants.*;

@Service
@RequiredArgsConstructor
public class TicTacToeServiceImpl implements GameService {

    private final GameValidator validator;
    private final GameRuleEngine ruleEngine;
    private final GameResponseFactory responseFactory;

    @Override
    public GameResponse initializeGame(Integer size) {
        int boardSize = (size != null && size >= DEFAULT_BOARD_SIZE) ? size : DEFAULT_BOARD_SIZE;
        int totalSpots = boardSize * boardSize;

        List<String> initialBoard = IntStream.range(MIN_INDEX, totalSpots)
                .mapToObj(String::valueOf)
                .toList();

        return responseFactory.create(
                initialBoard,
                GameResponse.StatusEnum.IN_PROGRESS,
                GameResponse.NextPlayerEnum.X,
                GAME_INIT_MESSAGE.formatted(boardSize, boardSize)
        );
    }

    @Override
    public GameResponse playMove(GameRequest request) {
        validator.validate(request);

        var board = request.getBoard();
        var currentPlayerStr = request.getCurrentPlayer().getValue();
        var currentPlayerChar = currentPlayerStr.charAt(MIN_INDEX);

        board.set(request.getPosition(), currentPlayerStr);
        char[] boardArray = String.join("", board).toCharArray();

        if (ruleEngine.isWinner(boardArray)) {
            return processWin(board, currentPlayerStr);
        }

        if (ruleEngine.isDraw(boardArray)) {
            return processDraw(board);
        }

        return processNextMove(board, currentPlayerChar);
    }

    private GameResponse processWin(List<String> board, String winningPlayer) {
        return responseFactory.create(
                board,
                GameResponse.StatusEnum.GAME_OVER_WIN,
                GameResponse.NextPlayerEnum.MINUS,
                GAME_WIN_MESSAGE.formatted(winningPlayer)
        );
    }

    private GameResponse processDraw(List<String> board) {
        return responseFactory.create(
                board,
                GameResponse.StatusEnum.GAME_OVER_DRAW,
                GameResponse.NextPlayerEnum.MINUS,
                GAME_DRAW_MESSAGE
        );
    }

    private GameResponse processNextMove(List<String> board, char currentPlayerChar) {
        char nextChar = ruleEngine.determineNextPlayer(currentPlayerChar);
        var nextPlayerEnum = GameResponse.NextPlayerEnum.fromValue(String.valueOf(nextChar));

        return responseFactory.create(
                board,
                GameResponse.StatusEnum.IN_PROGRESS,
                nextPlayerEnum,
                GAME_MOVE_ACCEPTED_MESSAGE
        );
    }
}