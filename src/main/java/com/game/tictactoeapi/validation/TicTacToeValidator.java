package com.game.tictactoeapi.validation;

import com.game.tictactoeapi.model.GameRequest;
import com.game.tictactoeapi.validation.rules.GameValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicTacToeValidator implements GameValidator {

    private final List<GameValidationRule> validationRules;

    @Override
    public void validate(GameRequest request) {
        for (var rule : validationRules) {
            rule.validate(request);
        }
    }
}