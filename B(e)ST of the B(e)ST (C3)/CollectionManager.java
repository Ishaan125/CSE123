// Ishaan Srivastava 
// 12/5/25
// CSE 123 
// C3: B(e)ST of the B(e)ST
// TA: Aidan Suen
 
import java.util.*;
import java.io.*;

// This class manages a collection of Game objects and contains methods to add, check for 
// containment, convert to string, save to a file, and filter games based on certain criteria.
public class CollectionManager {
    private GameNode overallRoot;

    // Behavior: Constructs an empty CollectionManager.
    public CollectionManager() {
        overallRoot = null;
    }

    // Behavior: Constructs a CollectionManager by reading Game data from the provided Scanner.
    //           Any spaces in the input(not between attributes) must be replaced by 
    //           something else like underscores.
    // Parameters: input - the Scanner object used to read Game data.
    //             Assumes that the inputted scanner is in a valid format.
    public CollectionManager(Scanner input) {
        overallRoot = populateTree(input, overallRoot);
    }

    // Behavior: Populates a CollectionManager with Game objects read from the Scanner.
    // Parameters: input - the Scanner object used to read Game data.
    //             node - the current node in the CollectionManager.
    // Returns: the root node of the populated CollectionManager.
    private GameNode populateTree(Scanner input, GameNode node) {
        if (input == null || !input.hasNextLine()) {
            return node;
        } 

        String text = input.nextLine().trim();
        String[] parts = text.split("\\s+");
        Game game = new Game(Double.parseDouble(parts[0]), Integer.parseInt(parts[1]), 
            Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
        node = addHelp(game, node);    

        return populateTree(input, node);
    }

    // Behavior: Adds a Game object to the collection.
    // Parameters: game - the Game object to be added.
    public void add(Game game) {
        overallRoot = addHelp(game, overallRoot);
    }

    // Behavior: Helper method to add a Game object to the CollectionManager.
    // Parameters: game - the Game object to be added.
    //             node - the current node in the CollectionManager.
    // Returns: the root node of the updated CollectionManager.
    private GameNode addHelp(Game game, GameNode node) {
        if (node == null) {
            return new GameNode(game);
        }

        if (game.compareTo(node.game) < 0) {
            node.left = addHelp(game, node.left);
        } 
        else if (game.compareTo(node.game) > 0) {
            node.right = addHelp(game, node.right);
        }

        return node;
    }

    // Behavior: Checks if the collection contains the specified Game object.
    // Parameters: game - the Game object to be checked.
    // Returns: true if the collection contains the Game object, false otherwise.
    public boolean contains(Game game) {
        return containsHelp(game, overallRoot);
    }

    // Behavior: Helper method to check if the collection contains the specified Game object.
    // Parameters: game - the Game object to be checked.
    //             node - the current node in the CollectionManager.
    // Returns: true if the collection contains the Game object, false otherwise.
    private boolean containsHelp(Game game, GameNode node) {
        if (node == null) {
            return false;
        }

        if (game.equals(node.game)) {
            return true;
        }
        else if (game.compareTo(node.game) < 0) {
            return containsHelp(game, node.left);
        }
        else {
            return containsHelp(game, node.right);
        }
    }

    // Behavior: Returns a sorted string representation of the collection.
    //           Format: Rating=..., Copies Sold=..., Studio=..., Name=...,
    //           Release Date=..., Genre=...
    // Returns: a string representation of the collection.
    public String toString() {
        return toStringHelp(overallRoot);
    }

    // Behavior: Helper method to generate a sorted string representation of the collection.
    // Parameters: node - the current node in the CollectionManager.
    // Returns: a string representation of the collection.
    private String toStringHelp(GameNode node) {
        if (node == null) {
            return "";
        }
        String result = "";
        result += toStringHelp(node.left);
        result += node.toString() + "\n";
        result += toStringHelp(node.right);
        return result;
    }

    // Behavior: Saves the collection to the specified PrintStream in a format suitable
    //           for later loading.
    // Parameters: output - the PrintStream object used to save the collection.
    public void save(PrintStream output) {
        saveHelp(overallRoot, output);
    }

    // Behavior: Helper method to save the collection to the specified PrintStream.
    // Parameters: node - the current node in the CollectionManager.
    //             output - the PrintStream object used to save the collection.
    private void saveHelp(GameNode node, PrintStream output) {
        if (node != null) {
            output.println(node.game.toDataString());
            saveHelp(node.left, output);
            saveHelp(node.right, output);
        }
    }

    // Creative Extension: Filter Best Games
    // Behavior: Returns a list of the games that have at least 'num' copies sold
    //           if an int is passed into the method, or a list of the games that
    //           have at least a rating 'num' if a double is passed in.
    // Parameters: num - the threshold for filtering games.
    // Returns: a list of the filtered games.
    public List<Game> filterExtension(int num) {
        List<Game> result = new ArrayList<>();
        filterPopularHelp(overallRoot, num, result);
        return result;
    }
    public List<Game> filterExtension(double num) {
        List<Game> result = new ArrayList<>();
        filterRatingHelp(overallRoot, num, result);
        return result;
    }

    // Behavior: Helper method for filtering games based on copies sold.
    // Parameters: node - the current node in the CollectionManager.
    //             sold - the minimum number of copies sold.
    //             result - the list to store the filtered games.
    private void filterPopularHelp(GameNode node, int sold, List<Game> result) {
        if (node != null) {
            filterPopularHelp(node.left, sold, result);
            if (node.game.getCopiesSold() >= sold) {
                result.add(node.game);
            }
            filterPopularHelp(node.right, sold, result);
        }
    }
    // Difference: This method filters based on rating.
    // Parameters: node - the current node in the CollectionManager.
    //             rating - the minimum rating.
    //             result - the list to store the filtered games.
    private void filterRatingHelp(GameNode node, double rating, List<Game> result) {
        if (node != null) {
            filterRatingHelp(node.left, rating, result);
            if (node.game.getRating() >= rating) {
                result.add(node.game);
            }
            filterRatingHelp(node.right, rating, result);
        }
    }
        
    private static class GameNode {
        public final Game game;
        public GameNode left;
        public GameNode right;

        // Behavior: Constructs a GameNode with the specified Game object.
        // Parameters: game - the Game object to be stored in the node.
        public GameNode(Game game) {
            this(game, null, null);
        }

        // Behavior: Constructs a GameNode with the specified Game object, left and right child.
        // Parameters: game - the Game object to be stored in the node.
        //             left - the left child of the node.
        //             right - the right child of the node.
        public GameNode(Game game, GameNode left, GameNode right) {
            this.game = game;
            this.left = left;
            this.right = right;
        }

        // Behavior: Returns a string representation of the Game object stored in the node.
        //           Format: Rating=..., Copies Sold=..., Studio=..., Name=...,
        //           Release Date=..., Genre=...
        // Returns: a string representation of the Game object.
        @Override
        public String toString() {
            return game.toString();
        }
    }
}
