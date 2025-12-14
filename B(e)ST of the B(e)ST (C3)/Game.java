// Ishaan Srivastava 
// 12/5/25
// CSE 123 
// C3: B(e)ST of the B(e)ST
// TA: Aidan Suen

import java.util.Scanner;

// This class represents a game object with specific attributes such as
// rating, copies sold, release date, studio, name, and genre.
public class Game implements Comparable<Game> {
    private final double rating;
    private final int copiesSold;
    private final int releaseDate;
    private final String studio;
    private final String name;
    private final String genre;

     // Behavior: Constructs a Game object with the specified rating, copies sold, release date, 
     //           studio, name, and genre.
     // Parameters: rating - the rating of the game out of 5.0
     //             copiesSold - the number of copies sold of the game
     //             releaseDate - the release date of the game (don't need month/day, just year)
     //             studio - the studio that developed the game
     //             name - the name of the game
     //             genre - the genre of the game 
    public Game(double rating, int copiesSold, int releaseDate, String studio, 
        String name, String genre) {

        this.rating = rating;
        this.copiesSold = copiesSold;
        this.releaseDate = releaseDate;
        this.studio = studio;
        this.name = name;
        this.genre = genre;
    }

    // Constructs a Game object by copying the properties of another Game object.
    // Assumes that the inputted game is not null.
    public Game(Game game) {
        this(game.rating, game.copiesSold, game.releaseDate, game.studio, game.name, game.genre);
    }

    // Behavior: Returns the number of copies sold of the game.
    // Returns: the number of copies sold of the game (int).
    public int getCopiesSold() {
        return this.copiesSold;
    }

    // Behavior: Returns the rating of the game.
    // Returns: the rating of the game (double).
    public double getRating() {
        return this.rating;
    }

    // Behavior: Prompts the user for input to create a new Game object.
    // Parameters: input - the Scanner object used to read user input.
    //             Assumes that the inputted scanner is not null.
    // Returns: a new Game with the provided attributes from user input.
    public static Game parse(Scanner input) {
        System.out.print("What is the rating of your game out of 5.0? ");
        double rating = Double.parseDouble(input.nextLine());

        System.out.print("How many copies has your game sold? ");
        int copiesSold = Integer.parseInt(input.nextLine());

        System.out.print("When was the release year of your game? ");
        int releaseDate = Integer.parseInt(input.nextLine());

        System.out.print("Which studio developed your game? ");
        String studio = input.nextLine();

        System.out.print("What is the name of your game? ");
        String name = input.nextLine();

        System.out.print("What is the genre of your game? ");
        String genre = input.nextLine();

        return new Game(rating, copiesSold, releaseDate, studio, name, genre);
    }

    // Behavior: Returns a String representation of the Game's attributes.
    // Returns: a String representation of the Game's attributes.
    @Override
    public String toString() {
        return "Rating=" + this.rating + ", Copies Sold=" + this.copiesSold + ", Studio=" + 
            this.studio + ", Name=" + this.name + ", Release Date=" + this.releaseDate + 
            ", Genre=" + this.genre;
    }

    // Behavior: Returns a space-separated data string. Format matches input files:
    //           rating copiesSold releaseDate studio name genre
    // Returns: a space-separated data string of the Game's attributes.
    public String toDataString() {
        return this.rating + " " + this.copiesSold + " " + this.releaseDate + " "
            + this.studio + " " + this.name + " " + this.genre;
    }

    // Behavior: Compares this Game object to another Game object based on their attributes:
    //           Rating (descending), Copies Sold (descending), Release Date (descending),
    //           Studio (ascending lexicographically), Name (ascending lexicographically), 
    //           Genre (ascending lexicographically).
    // Parameters: other - the Game object to compare to
    // Returns: a negative integer, zero, or a positive integer as this object is 
    //          less than, equal to, or greater than the other object.
    @Override
    public int compareTo(Game other) {
        if (this.rating != other.rating) {
            return Double.compare(other.rating, this.rating);
        }
        else if (this.copiesSold != other.copiesSold) {
            return other.copiesSold - this.copiesSold;
        }
        else if (this.releaseDate != other.releaseDate) {
            return other.releaseDate - this.releaseDate;
        }
        else if (!this.studio.equals(other.studio)) {
            return this.studio.compareTo(other.studio);
        } 
        else if (!this.name.equals(other.name)) {
            return this.name.compareTo(other.name);
        }
        else  {
            return this.genre.compareTo(other.genre);
        }
    }

    // Behavior: Checks if this Game object is equal to another object based on their attributes.
    // Parameters: o - the object to compare to
    // Returns: true if the objects are equal, false otherwise.
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } 
        
        else if (o instanceof Game) {
            Game otherGame = (Game) o;
            return this.rating == otherGame.rating && this.copiesSold == otherGame.copiesSold
                && this.releaseDate == otherGame.releaseDate
                && this.studio.equals(otherGame.studio) && this.name.equals(otherGame.name)
                && this.genre.equals(otherGame.genre);     
        } 
        
        else {
            return false;
        }
    }

    // Behavior: Returns a hash code value for the Game object based on its attributes.
    // Returns: a hash code value for the Game object (int).
    @Override
    public int hashCode() {
        return 31 * (Double.hashCode(this.rating) + Integer.hashCode(this.copiesSold) +
            Integer.hashCode(this.releaseDate) + this.studio.hashCode() + this.name.hashCode() +
            this.genre.hashCode());
    }
}
