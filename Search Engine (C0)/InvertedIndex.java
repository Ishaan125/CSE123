// Ishaan Srivastava 
// 10/1/25
// CSE 123 
// C0: Search Engine

// This class is used to map each unique word in a list of Media documents
// to the Media documents that contain that word.

import java.util.*;

public class InvertedIndex {
    public static void main(String[] args) {
        List<Media> docs = List.of(
            new Book("Mistborn", List.of("Brandon Sanderson"),
                     new Scanner("Epic fantasy worldbuildling content")),
            new Book("Farenheit 451", List.of("Ray Bradbury"),
                     new Scanner("Realistic \"sci-fi\" content")),
            new Book("The Hobbit", List.of("J.R.R. Tolkein"),
                     new Scanner("Epic fantasy quest content"))
        );
        
        Map<String, Set<Media>> result = createIndex(docs);
        System.out.println(docs);
        System.out.println();
        System.out.println(result);
    }

    // Behavior: creates an inverted index from a list of Media documents. It maps each unique word
    //      to a set of Media objects that contain that word.
    // Exceptions: none, Returns: a TreeMap where each key is a unique word from the documents and
    //      each value is a set of Media objects that contain that word(Map<String, Set<Media>>),
    // Parameters: docs(a list of Media documents to be indexed)
    public static Map<String, Set<Media>> createIndex(List<Media> docs) {
        Map<String, Set<Media>> indexes = new TreeMap<>();
        for (Media item : docs) {
            for (String word : item.getContent()) {
                word = word.toLowerCase();
                // Found this online for removing punctuation except for apostrophes
                word = word.replaceAll("[^a-z0-9'-]", "");  
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
}