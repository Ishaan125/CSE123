// Ishaan Srivastava 
// 10/1/25 -> 10/10/25 -> 10/24/25
// CSE 123 
// C0: Search Engine
// TA: Aidan Suen

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
// import java.io.*;

// This class contains unit tests for the Book, InvertedIndex, and SearchClient classes.
public class Testing {

    @Test
    @DisplayName("Book string, list constructor")
    public void testBookStringList() {

        Book book = new Book("Title", List.of("Author 1", "Author 2"), new Scanner("Content"));

        // Tests the getters and toString method
        assertEquals("Title", book.getTitle());
        assertEquals(List.of("Author 1", "Author 2"), book.getArtists());
        assertEquals(List.of("Content"), book.getContent());
        assertEquals("Title by [Author 1, Author 2]", book.toString());

        book.addRating(-1);  // Testing if -1 gets rejected as a score
        book.addRating(4);
        book.addRating(7); 
        book.addRating(6);   
        // Tests the rounding and toString with ratings included
        assertEquals("Title by [Author 1, Author 2]: 5.67 (3 ratings)", book.toString());      
    }

    @Test
    @DisplayName("getNumRatings")
    public void testNumRatings() {
        Book book = new Book("Title", List.of("Author"), new Scanner("Content"));
        
        // Initially, there should be 0 ratings
        assertEquals(0, book.getNumRatings());

        // After adding ratings, the number should increase accordingly
        book.addRating(1);
        assertEquals(1, book.getNumRatings());
        book.addRating(1);
        assertEquals(2, book.getNumRatings());        
    }

    @Test
    @DisplayName("getAvgRating")
    public void testAvgRatings() {
        Book book = new Book("Title", List.of("Author"), new Scanner("Content"));

        // Initially, average rating should be 0
        assertEquals(0, book.getAverageRating());
        
        // After adding ratings, the average should be calculated correctly
        book.addRating(4);
        assertEquals(4, book.getAverageRating());
        book.addRating(5);
        assertEquals(4.5, book.getAverageRating());   
    }

    @Test
    @DisplayName("createIndex tests")
    public void testInvertedIndex() {
        Book mistborn = new Book("Mistborn", List.of("Brandon Sanderson"),
                                 new Scanner("Epic fantasy worldbuildling content"));
        Book farenheit = new Book("Farenheit 451", List.of("Ray Bradbury"),
                                  new Scanner("Realistic \"sci-fi\" content"));
        Book hobbit = new Book("The Hobbit", List.of("J.R.R. Tolkein"),
                               new Scanner("Epic fantasy quest content"));
        
        List<Media> books = List.of(mistborn, farenheit, hobbit);
        Map<String, Set<Media>> index = SearchClient.createIndex(books);
        
        // Check punctuation handling
        assertFalse(index.containsKey("sci-fi"));
        assertTrue(index.containsKey("\"sci-fi\""));

        // Checks inverted index functionality
        Set<Book> expected = Set.of(mistborn, hobbit);
        assertEquals(expected, index.get("fantasy"));
    }

    /*
    @Test
    @DisplayName("SearchClient tests")
    public void testSearchClient() throws FileNotFoundException{
        Book mistborn = new Book("Mistborn", List.of("Brandon Sanderson"),
                                 new Scanner("Epic fantasy worldbuildling content"));
        Book farenheit = new Book("Farenheit 451", List.of("Ray Bradbury"),
                                  new Scanner("Realistic \"sci-fi\" content"));
        Book hobbit = new Book("The Hobbit", List.of("J.R.R. Tolkein"),
                               new Scanner("Epic fantasy quest content"));
        
        List<Media> books = List.of(mistborn, farenheit, hobbit);
        Map<String, Set<Media>> index = SearchClient.createIndex(books);
        Set<Media> items = new HashSet<>(books);

        // assertEquals(SearchClient.search(index, "epic"), items);
        // assertEquals(SearchClient.search(index, "hobbit mistborn worldbuilding"), items);

        Book Alice = new Book("Alice's Adventures in Wonderland", List.of("Lewis Carroll"),
                     new Scanner(new File("books/Alice's Adventures in Wonderland.txt")));
        Book Dracula = new Book("Dracula", List.of("Bram Stoker"),
                                 new Scanner(new File("books/Dracula.txt")));
     
        List<Media> books2 = List.of(Alice, Dracula);
        Map<String, Set<Media>> index2 = SearchClient.createIndex(books2);
        Set<Media> items2 = new HashSet<>(books2);

        assertEquals(items2, SearchClient.search(index2, "Wonderland"));
    }
    */
}