// Ishaan Srivastava 
// 10/29/25 -> 11/7/25
// CSE 123 
// P1: Mini-Git
// TA: Aidan Suen

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
// import java.util.*;

// This class contains tests for the synchronize method in the Repository class.
// There's a test for each of the four cases: front, middle, empty, and end.
// Each test sets up two repositories, performs commits, and then synchronizes
// one repository with the other to check that the resulting state is what's expected.
public class Testing {
    private Repository repo1;
    private Repository repo2;

    // Occurs before each of the individual test cases
    // (creates new repos and resets commit ids)
    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    // Tests if synchronizing works when the first commit is in repo2(other)
    @Test
    @Timeout(1)
    @DisplayName("front synchronize()")
    public void frontSynchronize() throws InterruptedException {
        commitAll(repo2, new String[]{"First commit"});  // First commit in repo2
        commitAll(repo1, new String[]{"Second commit", "Third commit"});
        commitAll(repo2, new String[]{"Fourth commit", "Fifth commit"});

        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 5);

        testHistory(repo1, 5, new String[]{
            "First commit",
            "Second commit",
            "Third commit",
            "Fourth commit",
            "Fifth commit",
        });

    }

    // Tests synchronizing when commits are interweaved between repo1(this) and repo2(other)
    @Test
    @Timeout(1)
    @DisplayName("middle synchronize()")
    public void middleSynchronize() throws InterruptedException {
        commitAll(repo2, new String[]{"First commit"});  // Commit order is interweaved
        commitAll(repo1, new String[]{"Second commit"});
        commitAll(repo2, new String[]{"Third commit"});
        commitAll(repo1, new String[]{"Fourth commit"});
        commitAll(repo2, new String[]{"Fifth commit"});

        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 5);

        testHistory(repo1, 5, new String[]{
            "First commit",
            "Second commit",
            "Third commit",
            "Fourth commit",
            "Fifth commit"
        });
    }

    // Tests synchronizing when one or both repositories are empty
    @Test
    @Timeout(1)
    @DisplayName("empty synchronize()")
    public void emptySynchronize() throws InterruptedException {
        // empty repo2
        commitAll(repo1, new String[]{"Only commit in repo1"});
        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 1);

        testHistory(repo1, 1, new String[]{
            "Only commit in repo1"
        });

        // empty repo1
        repo1 = new Repository("repo1");
        commitAll(repo2, new String[]{"Only commit in repo2"});
        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 1);
        testHistory(repo1, 1, new String[]{
            "Only commit in repo2"
        });

        // empty repo1 and repo2
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 0);
    }

    // Tests synchronizing when the last commit is in repo2(other)
    @Test
    @Timeout(1)
    @DisplayName("end synchronize()")
    public void endSynchronize() throws InterruptedException {
        commitAll(repo2, new String[]{"First commit", "Second commit"});
        commitAll(repo1, new String[]{"Third commit", "Fourth commit"});
        repo2.commit("Fifth commit");  // Last commit in repo2

        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 5);

        testHistory(repo1, 5, new String[]{
            "First commit",
            "Second commit",
            "Third commit",
            "Fourth commit",
            "Fifth commit"
        });
    }

    /////////////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS (You don't have to use these if you don't want to!) //
    /////////////////////////////////////////////////////////////////////////////////

    // Commits all of the provided messages into the provided repo, making sure timestamps
    // are correctly sequential (no ties). If used, make sure to include
    //      'throws InterruptedException'
    // much like we do with 'throws FileNotFoundException'. 
    // repo and messages should be non-null.
    // Example useage:
    //
    // repo1:
    //      head -> null
    // To commit the messages "one", "two", "three", "four"
    //      commitAll(repo1, new String[]{"one", "two", "three", "four"})
    // This results in the following after picture
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void commitAll(Repository repo, String[] messages) throws InterruptedException {
        // Commit all of the provided messages
        for (String message : messages) {
            int size = repo.getRepoSize();
            repo.commit(message);
            
            // Make sure exactly one commit was added to the repo
            assertEquals(size + 1, repo.getRepoSize(),
                         String.format("Size not correctly updated after commiting message [%s]",
                                       message));

            // Sleep to guarantee that all commits have different time stamps
            Thread.sleep(2);
        }
    }

    // Makes sure the given repositories history is correct up to 'n' commits, checking against
    // all commits made in order. repo and allCommits should be non-null.
    // Example useage:
    //
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //      (Commits made in the order ["one", "two", "three", "four"])
    // To test the getHistory() method up to n=3 commits this can be done with:
    //      testHistory(repo1, 3, new String[]{"one", "two", "three", "four"})
    // Similarly, to test getHistory() up to n=4 commits you'd use:
    //      testHistory(repo1, 4, new String[]{"one", "two", "three", "four"})
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void testHistory(Repository repo, int n, String[] allCommits) {
        int totalCommits = repo.getRepoSize();
        assertTrue(n <= totalCommits,
                   String.format("Provided n [%d] too big. Only [%d] commits",
                                 n, totalCommits));
        
        String[] nCommits = repo.getHistory(n).split("\n");
        
        assertTrue(nCommits.length <= n,
                   String.format("getHistory(n) returned more than n [%d] commits", n));
        assertTrue(nCommits.length <= allCommits.length,
                   String.format("Not enough expected commits to check against. " +
                                 "Expected at least [%d]. Actual [%d]",
                                 n, allCommits.length));
        
        for (int i = 0; i < n; i++) {
            String commit = nCommits[i];
            System.out.println("Checking commit: " + commit);

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left
            int backwardsIndex = totalCommits - 1 - i;
            String commitMessage = allCommits[backwardsIndex];

            assertTrue(commit.contains(commitMessage),
                       String.format("Commit [%s] doesn't contain expected message [%s]",
                                     commit, commitMessage));
            assertTrue(commit.contains("" + backwardsIndex),
                       String.format("Commit [%s] doesn't contain expected id [%d]",
                                     commit, backwardsIndex));
        }
    }
}
