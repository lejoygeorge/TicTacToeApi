package com.game.tictactoeapi.service;

import com.game.tictactoeapi.factory.GameResponseFactory;
import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.model.GameResponse;
import com.game.tictactoeapi.ruleengine.GameRuleEngine;
import com.game.tictactoeapi.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${game.init.message}")
    private String initMessage;

    @Value("${game.win.message}")
    private String winMessage;

    @Value("${game.draw.message}")
    private String drawMessage;

    @Value("${game.move.accepted.message}")
    private String moveAcceptedMessage;

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
                initMessage.formatted(boardSize, boardSize)
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
                winMessage.formatted(winningPlayer)
        );
    }

    private GameResponse processDraw(List<String> board) {
        return responseFactory.create(
                board,
                GameResponse.StatusEnum.GAME_OVER_DRAW,
                GameResponse.NextPlayerEnum.MINUS,
                drawMessage
        );
    }

    private GameResponse processNextMove(List<String> board, char currentPlayerChar) {
        char nextChar = ruleEngine.determineNextPlayer(currentPlayerChar);
        var nextPlayerEnum = GameResponse.NextPlayerEnum.fromValue(String.valueOf(nextChar));

        return responseFactory.create(
                board,
                GameResponse.StatusEnum.IN_PROGRESS,
                nextPlayerEnum,
                moveAcceptedMessage
        );
    }
}