// Ishaan Srivastava 
// 10/1/25 -> 10/10/25 -> 10/24/25
// CSE 123 
// C0: Search Engine
// TA: Aidan Suen

import java.io.*;
import java.util.*;

// This class allows users to find and rate books within BOOK_DIRECTORY containing certain terms.
// There are methods to create an inverted index and search that index with a query.
public class SearchClient {
    public static final String BOOK_DIRECTORY = "./books";
    private static final Random RAND = new Random();

    // Some class constants you can play around with to give random ratings to the uploaded books!
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final int MIN_NUM_RATINGS = 1;
    public static final int MAX_NUM_RATINGS = 100;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        List<Media> media = new ArrayList<>(loadBooks());

        Map<String, Set<Media>> index = createIndex(media);

        System.out.println("Welcome to the CSE 123 Search Engine!");
        String command = "";
        while (!command.equalsIgnoreCase("quit")) {
            System.out.println("What would you like to do? [Search, Rate, Quit]");
            System.out.print("> ");
            command = console.nextLine();

            if (command.equalsIgnoreCase("search")) {
                searchQuery(console, index);
            } else if (command.equalsIgnoreCase("rate")) {
                addRating(console, media);
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Invalid command, please try again.");
            }
        }
        System.out.println("See you next time!");
    }

    // Behavior: creates an inverted index from a list of Media documents.
    // Exceptions: none, Returns: an ordered map(based on average rating from high to low) 
    //             where each key is a unique word(lowercase/case-insensitive) 
    //             from the documents and each value is a Set of
    //             Media objects that contain that word(Map<String, Set<Media>>),
    // Parameters: docs(a list of Media documents to be indexed)
    public static Map<String, Set<Media>> createIndex(List<Media> docs) {
        Map<String, Set<Media>> indexes = new TreeMap<>();
        for (Media item : docs) {
            for (String word : item.getContent()) {
                word = word.toLowerCase();
                if (!indexes.containsKey(word)) {
                    Set<Media> singleMedia = new HashSet<>();
                    singleMedia.add(item);
                    indexes.put(word, singleMedia);
                }
                else {
                    indexes.get(word).add(item);
                }
            }
        }
        return indexes;
    }

    // Behavior: searches the inverted index for Media that match the query(case-insensitive).
    //      The search results are ranked on the criteria:
    //      +5 points if the query contains the author/title,
    //      +1 point for each word that's in both the query and media (sort by points)
    //      The top 10 results are returned.
    // Exceptions: none, Returns: a set of Media objects that match the query(Set<Media>),
    // Parameters: index(an inverted index with terms and the Set of media containing those terms),
    //             query(the search query)
    public static Set<Media> search(Map<String, Set<Media>> index, String query) {
        Map<Media, Integer> scores = new HashMap<>();
        // found "\\s+" online for splitting on whitespace
        String[] queryWords = query.toLowerCase().split("\\s+");

        // Iterate directly over the map's values (sets of Media)
        for (Set<Media> mediaSet : index.values()) {
            for (Media item : mediaSet) {
                if (!scores.containsKey(item)) {
                    int score = 0;
                    // +1 for each matching word in the content
                    for (String q : queryWords) {
                        for (String contentWord : item.getContent()) {
                            if (q.equals(contentWord.toLowerCase())) {
                                score += 1;
                            }
                        }
                        // +5 for matching artist
                        for (String artist : item.getArtists()) {
                            if (q.equals(artist.toLowerCase())) {
                                score += 5;
                            }
                        }
                        // +5 for matching title
                        if (q.equals(item.getTitle().toLowerCase())) {
                            score += 5;
                        }
                    }
                    scores.put(item, score);
                }
            }
        }

        List<Map.Entry<Media, Integer>> topEntries = new ArrayList<>();
        List<Map.Entry<Media, Integer>> entries = new ArrayList<>(scores.entrySet());

        // Sorts by score descending, max of 10 search results
        while (topEntries.size() < 10 && !entries.isEmpty()) {
            Map.Entry<Media, Integer> best = entries.get(0);
            for (Map.Entry<Media, Integer> entry : entries) {
                if (entry.getValue() > best.getValue()) {
                    best = entry;
                }
            }
            topEntries.add(best);
            entries.remove(best);
        }

        Set<Media> results = new TreeSet<>();
        for (Map.Entry<Media, Integer> entry : topEntries) {
            results.add(entry.getKey());
        }
        return results;
    }
    
    // Allows the user to search a specific query using the provided 'index' to find appropriate
    // Media entries.
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null
    //   index - an inverted index mapping terms to the Set of media containing those terms.
    //           Should be non-null
    public static void searchQuery(Scanner console, Map<String, Set<Media>> index) {
        System.out.println("Enter query:");
        System.out.print("> ");
        String query = console.nextLine();

        Set<Media> result = search(index, query);
        
        if (result.isEmpty()) {
            System.out.println("\tNo results!");
        } else {
            for (Media m : result) {
                System.out.println("\t" + m.toString());
            }
        }
    }

    // Allows the user to add a rating to one of the options wthin 'media'
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null.
    //   media - list of all media options loaded into the search engine. Should be non-null.
    public static void addRating(Scanner console, List<Media> media) {
        for (int i = 0; i < media.size(); i++) {
            System.out.println("\t" + i + ": " + media.get(i).toString());
        }
        System.out.println("What would you like to rate (enter index)?");
        System.out.print("> ");
        int choice = Integer.parseInt(console.nextLine());
        if (choice < 0 || choice >= media.size()) {
            System.out.println("Invalid choice");
        } else {
            System.out.println("Rating [" + media.get(choice).getTitle() + "]");
            System.out.println("What rating would you give?");
            System.out.print("> ");
            int rating = Integer.parseInt(console.nextLine());
            media.get(choice).addRating(rating);
        }
    }

    // Loads all books from BOOK_DIRECTORY. Assumes that each book starts with two lines -
    //      "Title: " which is followed by the book's title
    //      "Author: " which is followed by the book's author
    //
    // Returns:
    //   A list of all book objects corresponding to the ones located in BOOK_DIRECTORY
    public static List<Media> loadBooks() throws FileNotFoundException {
        List<Media> ret = new ArrayList<>();
        
        File dir = new File(BOOK_DIRECTORY);
        for (File f : dir.listFiles()) {
            Scanner sc = new Scanner(f, "utf-8");
            String title = sc.nextLine().substring("Title: ".length());
            List<String> author = List.of(sc.nextLine().substring("Author: ".length()));

            Media book = new Book(title, author, sc);

            // Adds random ratings to 'book' based on the class constants. 
            // Feel free to comment this out.
            int minRating = RAND.nextInt(MAX_RATING - MIN_RATING + 1) + MIN_RATING;
            addRatings(minRating, Math.min(MAX_RATING,RAND.nextInt(MAX_RATING - minRating + 1) + minRating),
                        RAND.nextInt(MAX_NUM_RATINGS - MIN_NUM_RATINGS) + MIN_NUM_RATINGS, book);
            ret.add(book);
        }

        return ret;
    }

    // Adds ratings to the provided media numRatings amount of times. Each rating is a random int
    // between minRating and maxRating (inclusive).
    private static void addRatings(int minRating, int maxRating, int numRatings, Media media) {
        for (int i = 0; i < numRatings; i++) {
            media.addRating(RAND.nextInt(maxRating - minRating + 1) + minRating);
        }
    }
}