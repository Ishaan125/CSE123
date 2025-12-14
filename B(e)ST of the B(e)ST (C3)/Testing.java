// Ishaan Srivastava 
// 12/5/25
// CSE 123 
// C3: B(e)ST of the B(e)ST
// TA: Aidan Suen

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

// This class contains unit tests for the CollectionManager class. There's tests for all public
// methods including constructors, add, contains, toString, save, and the creative extension
// filter methods.
public class Testing {
    
    @Test
    @DisplayName("Test Empty Constructor")
    public void testEmptyConstructor() {
        CollectionManager cm = new CollectionManager();
        assertEquals("", cm.toString());
    }

    @Test
    @DisplayName("Test Scanner Constructor and toString Method")
    public void testScannerConstructor() {
        String data = "4.5 1000000 2020 StudioA GameA Action\n" +
                      "3.8 500000 2019 StudioB GameB Adventure\n";
        Scanner scanner = new Scanner(data);
        CollectionManager cm = new CollectionManager(scanner);
        String expected = "Rating=4.5, Copies Sold=1000000, Studio=StudioA, Name=GameA, " +
            "Release Date=2020, Genre=Action\n" + "Rating=3.8, Copies Sold=500000, " + 
            "Studio=StudioB, Name=GameB, Release Date=2019, Genre=Adventure\n";
        assertEquals(expected, cm.toString());
    }

    @Test
    @DisplayName("Test Add and Contains Methods")
    public void testAddAndContains() {
        CollectionManager cm = new CollectionManager();
        Game game1 = new Game(4.5, 1000000, 2020, "StudioA", "GameA", "Action");
        Game game2 = new Game(3.8, 500000, 2019, "StudioB", "GameB", "Adventure");
        cm.add(game1);
        cm.add(game2);
        assertTrue(cm.contains(game1));
        assertTrue(cm.contains(game2));
        Game game3 = new Game(4.0, 750000, 2021, "StudioC", "GameC", "RPG");
        assertFalse(cm.contains(game3));
    }

    @Test
    @DisplayName("Test Adding Duplicate Game")
    public void testAddDuplicate() {
        CollectionManager cm = new CollectionManager();
        Game game1 = new Game(4.5, 1000000, 2020, "StudioA", "GameA", "Action");
        cm.add(game1);
        cm.add(game1); // Adding duplicate
        assertEquals(1, cm.toString().split("\n").length); // Should still be only one game
    }   

    @Test
    @DisplayName("Test Contains on Empty Collection")
    public void testContainsEmpty() {
        CollectionManager cm = new CollectionManager();
        Game game1 = new Game(4.5, 1000000, 2020, "StudioA", "GameA", "Action");
        assertFalse(cm.contains(game1));
    }

    @Test
    @DisplayName("Test Save method")
    public void testSaveMethod() throws java.io.IOException {
        CollectionManager cm = new CollectionManager();
        Game game1 = new Game(4.5, 1000000, 2020, "StudioA", "GameA", "Action");
        Game game2 = new Game(3.8, 500000, 2019, "StudioB", "GameB", "Adventure");
        cm.add(game1);
        cm.add(game2);
        
        PrintStream output = new PrintStream(new FileOutputStream("testing.txt"));
        cm.save(output);
        output.close();

        // Read saved file into a list of lines
        List<String> actualLines = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File("testing.txt"));
        while (fileScanner.hasNextLine()) {
            actualLines.add(fileScanner.nextLine());
        }
        fileScanner.close();

        List<String> expectedLines = new ArrayList<>();
        expectedLines.add(game1.toDataString());
        expectedLines.add(game2.toDataString());
        assertEquals(new HashSet<>(expectedLines), new HashSet<>(actualLines));
    }

    @Test
    @DisplayName("Test Creative Extension: Filter Best Games")
    public void testFilterBestGames() throws java.io.IOException {
        CollectionManager cm = new CollectionManager();
        Game game1 = new Game(4.5, 1000000, 2020, "StudioA", "GameA", "Action");
        Game game2 = new Game(3.8, 500000, 2019, "StudioB", "GameB", "Adventure");
        Game game3 = new Game(4.2, 750000, 2021, "StudioC", "GameC", "RPG");
        cm.add(game1);
        cm.add(game2);
        cm.add(game3);
        // Filter games with at least 800,000 copies sold
        List<Game> mostPopular = cm.filterExtension(800000);
        assertTrue(mostPopular.contains(game1));
        assertFalse(mostPopular.contains(game2));
        assertFalse(mostPopular.contains(game3));

        // Filter games with at least 4.0 rating
        List<Game> highRatedGames = cm.filterExtension(4.0); 
        assertTrue(highRatedGames.contains(game1));
        assertTrue(highRatedGames.contains(game3));
        assertFalse(highRatedGames.contains(game2));
    }
}