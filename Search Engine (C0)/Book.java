// Ishaan Srivastava 
// 10/1/25 -> 10/10/25 -> 10/24/25
// CSE 123 
// C0: Search Engine
// TA: Aidan Suen

import java.util.*;

// This class represents a book object. It implements the Media and Comparable interfaces.
// There are methods to get the title, authors, content, add ratings, get number of ratings,
// get average rating, and a toString method for a string representation of the book.
public class Book implements Media, Comparable<Book> {
    private String title;
    private List<String> authors;
    private List<String> content;
    private List<Integer> ratings;

    // Behavior: constructs a book object which is a type of media
    // Exceptions: none, Returns: none
    // Parameters: title(the title of the book), authors(a list of authors of the book),
    //      content(a scanner containing the contents of the book with each token as a
    //      single piece of content)
    public Book(String title, List<String> authors, Scanner content) {
        this.title = title;
        this.authors = authors;
        this.content = new ArrayList<>();
        while (content.hasNext()) {
            this.content.add(content.next());
        }
        ratings = new ArrayList<>();
    }

    // Behavior: returns the title of the book
    // Exceptions: none, Returns: the title of the book(String), Parameters: none
    @Override
    public String getTitle() {
        return title;
    }

    // Behavior: returns the authors of the book
    // Exceptions: none, Returns: the list of authors of the book, Parameters: none
    @Override
    public List<String> getArtists() {
        return authors;
    }

    // Behavior: adds a rating to the ratings. Doesn't accept scores less than 0.
    // Exceptions: none, Returns: none, Parameters: score(the rating to get added)
    @Override
    public void addRating(int score) {
        if (score < 0) {
            System.out.println("Invalid Rating!");
        }
        else {
            ratings.add(score);
        }
    }

    // Behavior: returns the number of ratings
    // Exceptions: none, Returns: the number of ratings(int), Parameters: none
    @Override
    public int getNumRatings() {
        return ratings.size();
    }

    // Behavior: returns the average of all ratings. If there are no ratings, returns 0.
    // Exceptions: none, Returns: the average of all ratings(double), Parameters: none
    @Override
    public double getAverageRating() {
        if (ratings.size() == 0) {
            return 0.0;
        }

        double total = 0.0;
        for (int i : ratings) {
            total += i;
        }

        return total / ratings.size();
    }

    // Behavior: returns the content of the book.
    // Exceptions: none, Returns: the content of the book as a list of strings(List<String>). 
    // Parameters: none
    @Override
    public List<String> getContent() {
        return content;
    }

    // Behavior: returns a string representation of the book. No ratings: "<title> by [<authors>]".
    //      With ratings: "<title> by [<authors>]: <average rating> (<num ratings> ratings)". 
    //      The average rating is rounded to at most two decimal places.
    // Exceptions: none, Returns: a string representation of the book(String), Parameters: none
    @Override
    public String toString() {
        String text = title + " by " + authors.toString();

        if (getAverageRating() == 0) {
            return text;
        }

        // Algorithm for rounding to 2 decimal places
        double rounded = Math.round(getAverageRating() * 100.0) / 100.0;
        text += ": " + rounded + " (" + getNumRatings() + " ratings)";

        return text;
    }

    // Behavior: compares two book objects based on their average ratings.
    // Exceptions: none, Returns: 1(if this book's average rating is higher),
    //      -1(if this book's average rating is lower), 0(if both average
    //      ratings are equal), Parameters: other(the book to be compared to)
    @Override
    public int compareTo(Book other) {
        if (this.getAverageRating() - other.getAverageRating() > 0) {
            return 1;
        }
        else if (this.getAverageRating() - other.getAverageRating() < 0) {
            return -1;
        }
        else {
            return 0;
        }
    }
}