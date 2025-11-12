// Ishaan Srivastava 
// 10/15/25
// CSE 123 
// C1: Abstract Strategy Games
// TA: Aidan Suen

import java.util.*;

// This class represents a Connect Four game, extending the AbstractStrategyGame class.
// There's methods for instructions, state representation, move handling, and win conditions.
public class ConnectFour extends AbstractStrategyGame {
    public static final char PLAYER_1_TOKEN = 'X';
    public static final char PLAYER_2_TOKEN = 'O';
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;  
    public static final int TIE = 0;
    public static final int GAME_IS_OVER = -1;
    public static final int GAME_NOT_OVER = -1;
    public static final int ROWS = 6;  // standard Connect Four rows
    public static final int COLS = 7;  // standard Connect Four columns
    public static final int CONNECT = 4;  // number of tokens in a row needed to win

    private int[][] board;
    private boolean turn1;

    // Behavior: Creates a new Connect Four game.
    public ConnectFour() {
        board = new int[ROWS][COLS];
        turn1 = true;
    }

    // Behavior: Returns the instructions for playing Connect Four, Returns: result(String)
    @Override
    public String instructions() {
        String result = "";
        result += "Player 1 is X and goes first. Player 2 is O.\n";
        result += "Choose whether to add or remove a token by entering A or R followed by";
        result += " a space and the column number where you want to do the action (Ex: A 2).\n";
        result += "The tokens automatically go to the bottom row that is available.\n";
        result += "You can only remove your own tokens and only from the bottom of a column.\n";
        result += "Empty spaces in the board are available to place tokens.\n";
        result += "Spaces with an X are occupied by player 1,";
        result += " and spaces with an O are occupied by player 2.\n";
        result += "Columns are numbered 1 to 7 from left to right.\n";
        result += "The game ends when one player gets four of their tokens in a row or column.\n";
        result += "If the board is full and no player has four in a row, the game ends in a tie.";
        return result;    
    }
    
    // Behavior: Returns a string representation of the current game state, Returns: result(String)
    // Format: Each cell is represented by 'X' for player 1, 'O' for player 2, and ' ' for empty.
    // Example:
    // |   |   |   |   |   |   |   |
    // |   |   |   |   |   |   |   |
    // |   |   |   |   |   |   |   |    
    // |   |   |   |   |   |   |   |
    // |   |   | X | O |   |   |   |
    // | X | O | X | O | X |   |   |
    @Override
    public String toString() {
        String result = "";
        for (int row = 0; row < ROWS; row++) {
            result += "|";
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == PLAYER_1) {
                    result += " " + PLAYER_1_TOKEN + " |";
                } else if (board[row][col] == PLAYER_2) {
                    result += " " + PLAYER_2_TOKEN + " |";
                } else {
                    result += "   |";
                }
            }
            result += "\n";
        }
        return result;
    }
       
    // Behavior: Determines the winner of the game.
    // Returns: (int) 1 if player 1 wins, 2 if player 2 wins, 0 if tie, -1 if the game's not over.
    @Override
    public int getWinner() {
        boolean notOver = false;

        for (int row = ROWS - 1; row >= 0; row--) {
            int counterHorizontalWin1 = 0;
            int counterHorizontalWin2 = 0;

            for (int col = 0; col < COLS; col++) {
                int cell = board[row][col];
                if (cell == TIE) {
                    notOver = true;
                    // empty cell breaks any contiguous run
                    counterHorizontalWin1 = 0;
                    counterHorizontalWin2 = 0;
                }
                else if (cell == PLAYER_1) {
                    counterHorizontalWin1++;
                    counterHorizontalWin2 = 0;
                    if (counterHorizontalWin1 >= CONNECT) {
                        return PLAYER_1;
                    }
                }
                else if (cell == PLAYER_2) {
                    counterHorizontalWin2++;
                    counterHorizontalWin1 = 0;
                    if (counterHorizontalWin2 >= CONNECT) {
                        return PLAYER_2;
                    }
                }
            }
        }

        for (int col = 0; col < COLS; col++) {
            int counterVerticalWin1 = 0;
            int counterVerticalWin2 = 0;

            for (int row = ROWS - 1; row >= 0; row--) {
                int cell = board[row][col];
                if (cell == TIE) {
                    notOver = true;
                    counterVerticalWin1 = 0;
                    counterVerticalWin2 = 0;
                }
                else if (cell == PLAYER_1) {
                    counterVerticalWin1++;
                    counterVerticalWin2 = 0;
                    if (counterVerticalWin1 >= CONNECT) {
                        return PLAYER_1;
                    }
                }
                else if (cell == PLAYER_2) {
                    counterVerticalWin2++;
                    counterVerticalWin1 = 0;
                    if (counterVerticalWin2 >= CONNECT) {
                        return PLAYER_2;
                    }
                }
            }
        }

        if (notOver) {
            return GAME_NOT_OVER;
        }
        return TIE;
    }
    
    // Behavior: Returns the index of the player who will take the next turn.
    // Returns: (int) 1 if player 1's turn, 2 if player 2's turn, -1 if game is over.
    @Override
    public int getNextPlayer() {
        if (isGameOver()) {
            return GAME_IS_OVER;
        }
        
        if(turn1) {
            return PLAYER_1;
        } else {
            return PLAYER_2;
        }
    }

    // Behavior: Reads from the Scanner input if the user wants to add(A) or remove(R) a token and
    // the column number (1-7). Example inputs: "A 3", "R 5", "4"
    // (defaults to add, space between required if there are two characters).
    // Exceptions: Throws an IllegalArgumentException if the input is null or empty.
    // Parameters: (Scanner) input: the scanner to read user input from.
    // Returns: (String) A(add) or R(remove), a space, and the column number.
    @Override
    public String getMove(Scanner input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Null input!");
        }
        System.out.print("Add(A) or Remove(R) a token and Column number (example: R 3): ");
        String move = input.nextLine().trim();  // Works even if input has extra spaces
        if (move.isEmpty()) {
            throw new IllegalArgumentException("Empty input!");  // If input is just whitespace
        }
        String[] parts = move.split("\\s+");  // Split on whitespace
        if (parts.length == 1) {
            return "A " + parts[0];  // Default to adding if no action specified
        }
        return parts[0].toUpperCase() + " " + parts[1];  // Normalize action to uppercase
    }
    
    // Behavior: Either places the player's token in the specified column if it's valid or
    // removes the bottom token from the specified column and drops the rest of
    // the tokens in that column down if it's valid. (EXTENSION: Remove a token)
    // Exceptions: Throws an IllegalArgumentException if the input is null or the move is illegal.
    // Illegal moves include:
    // - Adding a token to a full column.
    // - Removing a token from an empty column.
    // - Invalid column number (not 1-7).
    // - Removing a token that doesn't belong to the player.
    // Parameters: (String) input: A or R followed by the column number.
    @Override
    public void makeMove(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Invalid input!");
        }
        int col = Integer.parseInt(input.split(" ")[1]) - 1;  // Convert to 0-indexed
        if (col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Invalid input!");
        } 
        boolean invalid = true;
        boolean add = input.split(" ")[0].equals("A");

        if (!add) {
            if (board[ROWS - 1][col] == 0 || (board[ROWS - 1][col] == 1 && !turn1) || 
                    (board[ROWS - 1][col] == 2 && turn1)) {
                throw new IllegalArgumentException("Invalid move!");
            }
            for (int row = ROWS - 1; row > 0; row--) {
                if (board[row][col] != 0) {
                    board[row][col] = board[row - 1][col];
                }
            }
            board[0][col] = 0;  // Clear the top cell after shifting down
            turn1 = !turn1;  // Switch turns
            invalid = false;  // Valid move made
        }

        else {
            // Start from the bottom row and find the first empty spot in the specified column
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[row][col] == 0) {
                    if (turn1) {
                        board[row][col] = PLAYER_1;
                    }
                    else {
                        board[row][col] = PLAYER_2;
                    }
                    turn1 = !turn1;  // Switch turns
                    invalid = false;  // Valid move made
                    row = -1;  // To exit the for loop
                }
            }
        }

        if (invalid) {
            throw new IllegalArgumentException("Invalid move! Column is already full!");
        } 
    }
}