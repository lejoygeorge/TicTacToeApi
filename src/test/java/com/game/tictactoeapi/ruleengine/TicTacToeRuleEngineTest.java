package com.game.tictactoeapi.ruleengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic-Tac-Toe Rule Engine Specifications")
class TicTacToeRuleEngineTest {

    private TicTacToeRuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new TicTacToeRuleEngine();
    }

    // Tests for isWinner()

    @Test
    @DisplayName("isWinner: Should return false when the board is in its initial state (no moves)")
    void isWinner_shouldReturnFalse_whenBoardIsInitialState() {
        char[] board = {'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        assertFalse(ruleEngine.isWinner(board), "Initial board should not have a winner");
    }

    @Test
    @DisplayName("isWinner: Should return false when 3 matching characters are not 'X' or 'O' (e.g., empty spots)")
    void isWinner_shouldReturnFalse_whenMatchingCharactersAreNotXOrO() {
        char[] board = {'1', '1', '1', '3', '4', '5', '6', '7', '8'};
        assertFalse(ruleEngine.isWinner(board), "Should not declare a win for non-player characters");
    }

    @Test
    @DisplayName("isWinner: Should return false when there are player moves but no winning combination exists")
    void isWinner_shouldReturnFalse_whenPlayerMovesButNoWin() {
        char[] board = {'X', 'O', '2', '3', '4', '5', '6', '7', '8'};
        assertFalse(ruleEngine.isWinner(board), "Board with partial non-winning moves should not have a winner");
    }

    @Test
    @DisplayName("isWinner: Should return false when the board is full but has no winning combination (draw)")
    void isWinner_shouldReturnFalse_whenBoardIsFullAndDraw() {
        char[] board = {'X', 'O', 'X', 'O', 'X', 'O', 'O', 'X', 'O'};
        assertFalse(ruleEngine.isWinner(board), "Full board with no matching combination should not have a winner");
    }

    @Test
    @DisplayName("isWinner: Should accurately detect horizontal wins across all rows")
    void isWinner_shouldReturnTrue_whenHorizontalWin() {
        char[] boardRow1 = {'X', 'X', 'X', '3', '4', '5', '6', '7', '8'};
        char[] boardRow2 = {'0', '1', '2', 'O', 'O', 'O', '6', '7', '8'};

        assertTrue(ruleEngine.isWinner(boardRow1), "Player X should win on first row");
        assertTrue(ruleEngine.isWinner(boardRow2), "Player O should win on second row");
    }

    @Test
    @DisplayName("isWinner: Should accurately detect vertical wins across all columns")
    void isWinner_shouldReturnTrue_whenVerticalWin() {
        char[] boardCol1 = {'X', '1', '2', 'X', '4', '5', 'X', '7', '8'};
        char[] boardCol3 = {'0', '1', 'O', '3', '4', 'O', '6', '7', 'O'};

        assertTrue(ruleEngine.isWinner(boardCol1), "Player X should win on first column");
        assertTrue(ruleEngine.isWinner(boardCol3), "Player O should win on third column");
    }

    @Test
    @DisplayName("isWinner: Should accurately detect diagonal wins")
    void isWinner_shouldReturnTrue_whenDiagonalWin() {
        char[] boardDiag1 = {'X', '1', '2', '3', 'X', '5', '6', '7', 'X'};
        char[] boardDiag2 = {'0', '1', 'O', '3', 'O', '5', 'O', '7', '8'};

        assertTrue(ruleEngine.isWinner(boardDiag1), "Player X should win on main diagonal");
        assertTrue(ruleEngine.isWinner(boardDiag2), "Player O should win on anti-diagonal");
    }

    // Tests for isDraw()

    @Test
    @DisplayName("isDraw: Should return true when the board is completely filled with 'X' and 'O'")
    void isDraw_shouldReturnTrue_whenBoardIsFullOfXAndO() {
        // A full board with no empty spaces ('0'-'8')
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertTrue(ruleEngine.isDraw(board), "Board full of X and O should be a draw");
    }

    @Test
    @DisplayName("isDraw: Should return false when there are still unplayed spots on the board")
    void isDraw_shouldReturnFalse_whenBoardIsNotFull() {
        // A board that still has numeric characters (unplayed spots)
        char[] board = {'X', 'O', 'X', '3', 'O', 'O', '6', 'X', '8'};
        assertFalse(ruleEngine.isDraw(board), "Board with numbers is not full, so not a draw");
    }

    @Test
    @DisplayName("isDraw: Should return false when the board is in its initial state")
    void isDraw_shouldReturnFalse_whenBoardIsInitialState() {
        char[] board = "012345678".toCharArray();
        assertFalse(ruleEngine.isDraw(board), "Initial board is entirely empty, not a draw");
    }

    // Tests for determineNextPlayer()

    @Test
    @DisplayName("determineNextPlayer: Should return 'O' when the current player is 'X'")
    void determineNextPlayer_shouldReturnO_whenCurrentIsX() {
        assertEquals('O', ruleEngine.determineNextPlayer('X'), "Next player after X should be O");
    }

    @Test
    @DisplayName("determineNextPlayer: Should return 'X' when the current player is 'O'")
    void determineNextPlayer_shouldReturnX_whenCurrentIsO() {
        assertEquals('X', ruleEngine.determineNextPlayer('O'), "Next player after O should be X");
    }
}