// Ishaan Srivastava 
// 10/15/25
// CSE 123 
// C1: Abstract Strategy Games
// TA: Aidan Suen

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
// import java.util.*;

// This class contains test cases for ConnectFour.
public class Testing {
    @Test
    @DisplayName("Win Condition")
    public void firstCaseTest() {
        AbstractStrategyGame game = new ConnectFour();

        // Initial state checks
        assertEquals(1, game.getNextPlayer(), "Player 1 not next player after construction");
        assertEquals(-1, game.getWinner(), "Winner incorrectly declared after construction");
        assertFalse(game.isGameOver(), "Game over immediately after construction");

        // Play a sequence of moves that gives player 1 a horizontal connect-4 on the bottom row.
        game.makeMove("A 1"); // P1
        game.makeMove("A 7"); // P2
        game.makeMove("R 1"); // P1
        game.makeMove("R 7"); // P2
        game.makeMove("A 1"); // P1
        game.makeMove("A 7"); // P2
        game.makeMove("A 5"); // P1
        game.makeMove("A 7"); // P2
        game.makeMove("A 3"); // P1
        game.makeMove("A 7"); // P2
        game.makeMove("A 3"); // P1
        game.makeMove("A 7"); // P2
        game.makeMove("A 4"); // P1
        game.makeMove("A 2"); // P2
        game.makeMove("A 6"); // P1 - This should make four in a row horizontally

        // Post-win state checks
        assertTrue(game.isGameOver(), "Game not over after winning move");
        assertEquals(1, game.getWinner(), "Player 1 should be the winner");
        assertEquals(-1, game.getNextPlayer(), "Next player should be -1 after game over");
    }

    @Test
    @DisplayName("Illegal Move (column full)")
    public void secondCaseTest() {
        AbstractStrategyGame game = new ConnectFour();

        // Fill column 0 by alternating moves, after 6 moves the column is full.
        for (int i = 0; i < 6; i++) {
            game.makeMove("A 1");
        }

        // The column is full, so attempting to play in the same column should throw an exception.
        assertThrows(IllegalArgumentException.class, () -> {
            game.makeMove("A 1");
        }, "IllegalArgumentException not thrown for playing in a full column");
    }

    @Test
    @DisplayName("Illegal Move (removing from empty column)")
    public void thirdCaseTest() {
        AbstractStrategyGame game = new ConnectFour();

        // Attempt to remove a token from an empty column (column 2).
        assertThrows(IllegalArgumentException.class, () -> {
            game.makeMove("R 2");
        }, "IllegalArgumentException not thrown for removing from an empty column");
    }
}
