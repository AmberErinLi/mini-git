import java.util.*;
import java.text.SimpleDateFormat;

// A version control system that tracks changes to documents over time. This class
// stores documents and their histories as a repository.
public class Repository {
    private Commit head;
    private String name;

    // Behavior:
    //   - Creates a new Repository with the given name
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given name is null or empty
    // Parameters:
    //   - String name - the name for this Repository
    public Repository(String name) {
        if (name == null || name == "") {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    // Returns:
    //   - The ID of the current head of this repository or null if the head is null
    public String getRepoHead() {
        if (head == null) {
            return null;
        }
        return head.id;
    }

    // Returns:
    //   - The number of commits in the repository
    public int getRepoSize() {
        int size = 0;
        Commit curr = head;
        while (curr != null) {
            size++;
            curr = curr.past;
        }
        return size;
    }

    // Returns:
    //   - A string representation of this repository, consisting of the repository's
    //     name and current head in the following format:
    //     "[name] - Current head: [head commit]"
    //   - Or if the repository has no commits:
    //     "[name] - No commits"
    public String toString() {
        if (head == null) {
            return this.name + " - No commits";
        }
        return this.name + " - Current head: " + this.head.toString();
    }

    // Behavior:
    //   - Returns whether the repository contains the commit with the given 'targetID'
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given 'targetId' is null
    // Returns:
    //   - true if the repository contains the commit with ID 'targetId'
    //   - false if the repository does not contain the commit with ID 'targetId'
    // Parameters:
    //   - String targetId - The ID that will be checked for a match in the repository's
    //     commits
    public boolean contains(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null");
        }
        Commit curr = head;
        while (curr != null) {
            if (curr.id.equals(targetId)) {
                return true;
            }
            curr = curr.past;
        }
        return false;
    }

    // Behavior:
    //   - Returns the history of this repository
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given number of commits, 'n',
    //     is non-positive
    // Returns:
    //   - A String consisting of the String representations of the most recent 'n'
    //     commits in this repository, with the most recent first. Each commit's String
    //     representation gets its own line
    //   - If there are fewer than n commits in this repository, all of their String
    //     representation's are returned
    //   - If there are no commits in this repository, the empty String is returned
    // Parameters:
    //   - int n - the number of commits to return, starting from the most recent one
    public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of commits must be positive");
        }
        String ret = "";
        int count = 0;
        Commit curr = head;
        while (curr != null && count < n) {
            ret += curr.toString() + "\n";
            count++;
            curr = curr.past;
        }
        return ret;
    }

    // Behavior:
    //   - Creates a new commit with the given message and adds it to this repository.
    //     This commit becomes the new head of the repository, preserving the history
    //     behind it.
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given 'message' is null
    // Returns:
    //   - The ID of the new commit
    // Parameters:
    //   - String message - the message describing the new commit
    public String commit(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        Commit newHead = new Commit(message, head);
        head = newHead;
        return newHead.id;
    }

    // Behavior:
    //   - If there is a commit with ID 'targetId' in this repository, removes it while
    //     maintaining the rest of the history
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given 'targetId' is null
    // Returns:
    //   - True if the commit with ID 'targetId' was successfully dropped
    //   - False if there is no commit that matches the given ID in the repository
    // Parameters:
    //   - String targetId - the commit with this ID will be dropped from the repository
    public boolean drop(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null");
        }
        if (head == null) {
            return false;
        } else if (head.id.equals(targetId)) {
            head = head.past;
            return true;
        }
        Commit curr = head;
        while (curr.past != null) {
            if (curr.past.id.equals(targetId)) {
                curr.past = curr.past.past;
                return true;
            }
            curr = curr.past;
        }
        return false;
    }

    // Behavior:
    //   - Takes all the commits in the other repository and moves them into this
    //     repository, combining the two histories such that chronological order is
    //     preserved. After the method executes, this repository will contain all the
    //     commits from both repositories in order from most recent to least recent, 
    //     and the other repository will be empty.
    //   - No changes are made if the other repository is empty.
    // Exceptions:
    //   - Throws an IllegalArgumentException if the other repository is null
    // Parameters:
    //   - Repository other - the other repository that will have its commits added
    //     chronologically to this repository
    public void synchronize(Repository other) {
        if (other == null) {
            throw new IllegalArgumentException("Other repository cannot be null");
        } 
        if (this.head == null) {
            this.head = other.head;
            other.head = null;
        } else if (other.head != null) {
            if (other.head.timeStamp > this.head.timeStamp) {
                Commit thisTemp = this.head;
                this.head = other.head;
                Commit otherTemp = other.head.past;
                this.head.past = thisTemp;
                other.head = otherTemp;
            }
            Commit curr = this.head;
            while (curr.past != null && other.head != null) {
                if (other.head.timeStamp > curr.past.timeStamp) {
                    Commit thisTemp = curr.past;
                    curr.past = other.head;
                    Commit otherTemp = other.head.past;
                    curr.past.past = thisTemp;
                    other.head = otherTemp;
                }
                curr = curr.past;
            }
            if (other.head != null) {
                curr.past = other.head;
                other.head = null;
            }
        }
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
