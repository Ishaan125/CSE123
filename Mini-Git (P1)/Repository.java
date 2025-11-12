// Ishaan Srivastava 
// 10/29/25 -> 11/7/25
// CSE 123 
// P1: Mini-Git
// TA: Aidan Suen

import java.util.*;
import java.text.SimpleDateFormat;

// This class represents a Mini-Git repository that can store commits
// and perform various operations such as committing, dropping, synchronizing,
// and retrieving commit history.
public class Repository {
    private String name;
    private Commit head;
    private int size;

    // Behavior: Constructs a new Repository with the given name,
    //           the repository has no commits initially.
    // Exceptions: Throws IllegalArgumentException if the name is null or empty.
    // Parameters: name - the name of the repository (String)
    public Repository(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Repository name cannot be null.");
        }
        this.name = name;
        this.head = null;
        this.size = 0;
    }

    // Behavior: Returns the id of the head of the repository.
    //           If there are no commits, returns null.
    // Returns: The id of the head commit or null if no commits exist.
    public String getRepoHead() {
        if (head == null) {
            return null;
        }
        return head.id;
    }

    // Behavior: Returns the number of commits in the repository.
    // Returns: The size of the repository
    public int getRepoSize() {
        return size;
    }

    // Behavior: Returns a string representation of the repository in this format:
    //          <name> - Current head: <head>
    //          If there are no commits: <name> - No commits
    // Returns: The string representation of the repository
    @Override
    public String toString() {
        if (size == 0) {
            return name + " - No commits";
        }
        return (name + " - Current head: " + head.toString());
    }

    // Behavior: Checks if a commit with the given id exists in the repository.
    // Exceptions: Throws IllegalArgumentException if targetId is null.
    // Parameters: targetId - the id of the commit to search for (String)
    // Returns: true if the commit exists, false otherwise.
    public boolean contains(String targetId) throws IllegalArgumentException {
        if (targetId == null) {
            throw new IllegalArgumentException();
        }

        Commit current = head;

        while (current != null) {
            if (current.id.equals(targetId)) {
                return true;
            }
            current = current.past;
        }
        return false;
    }

    // Behavior: Returns a string representation of the most recent n commits,
    //           starting from the most recent first.
    //           Each commit is separated by a newline character except the last one.
    //           If there are fewer than n commits, returns all commits. (when n is larger than
    //           the size of the repository)
    //           If there are no commits, returns an empty string.
    // Exceptions: Throws IllegalArgumentException if n is less than or equal to 0.
    // Parameters: n - the number of recent commits to retrieve (int)
    // Returns: A string representation of the most recent n commits.
    public String getHistory(int n) throws IllegalArgumentException{
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        if (head == null) {
            return "";
        }

        Commit current = head;
        String commits = current.toString();
        int counter = 1;

        while (counter < n && current.past != null) {
            current = current.past;
            commits += "\n" + current.toString();
            counter++;
        }

        return commits;
    }

    // Behavior: Creates a new commit with the given message
    //           and adds it to the repository as the new head.
    // Exceptions: Throws IllegalArgumentException if message is null. 
    // Parameters: message - the commit message (String)
    // Returns: The id of the newly created commit
    public String commit(String message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException();
        }
        head = new Commit(message, head);
        size++;
        return head.id;
    }

    // Behavior: Removes the commit with the given id from the repository.
    // Exceptions: Throws IllegalArgumentException if targetId is null.
    // Parameters: targetId - the id of the commit to remove (String)
    // Returns: true if the commit was found and removed, false if no commit matches the ID
    //          in the repository.
    public boolean drop(String targetId) throws IllegalArgumentException {
        if (targetId == null) {
            throw new IllegalArgumentException();
        }

        if (head == null) {  // Empty case
            return false;
        }
                
        if (head.id.equals(targetId)) {  // Front case
            head = head.past;
            size--;
            return true;
        }

        Commit current = head;

        while (current.past != null) {
            if (current.past.id.equals(targetId)) {
                current.past = current.past.past;  // Bypass the commit to remove it
                size--;
                return true;
            }
            current = current.past;
        }
        return false;
    }

    // Behavior: Merges the commits from another repository into this repository.
    //           The commits from both repositories are merged in chronological order
    //           based on their timestamps from most recent to least recent.
    //           After synchronization, the other repository is emptied.
    //           If this repository is empty, it takes all commits from the other repository.
    // Exceptions: Throws IllegalArgumentException if other is null.
    // Parameters: other - the repository to synchronize with (Repository)
    public void synchronize(Repository other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException();
        } 

        // Empty case
        if (this.head == null) {
            this.head = other.head;
            this.size = other.getRepoSize();
            other.head = null;
        }

        // Don't do anything if other repository is empty
        if (other.head != null) {
            Commit newHead;  // Will be set to either this.head or other.head
            Commit curr1 = this.head;
            Commit curr2 = other.head;

            // Pick the most recent commit to be the head (front)
            if (curr1.timeStamp >= curr2.timeStamp) {
                newHead = curr1;
                curr1 = curr1.past;
            } 
            else {
                newHead = curr2;
                curr2 = curr2.past;
            }
            Commit tail = newHead;  // Need a pointer to save the new head

            // Merge the rest (middle)
            while (curr1 != null && curr2 != null) {
                if (curr1.timeStamp >= curr2.timeStamp) {
                    tail.past = curr1;
                    curr1 = curr1.past;
                } 
                else {
                    tail.past = curr2;
                    curr2 = curr2.past;
                }
                tail = tail.past;
            }

            // Add whichever list still has the remaining commits (end)
            if (curr1 != null) {
                tail.past = curr1;
            } 
            else {
                tail.past = curr2;
            }

            this.head = newHead;
            this.size += other.size;
        }

        other.head = null;  // Empty the other repository
        other.size = 0;
    }

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
