package BookwormAdventures;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.*;
import java.util.NoSuchElementException;

// used to read csv file for english words
public class FileLineIterator implements Iterator<String> {
    private final BufferedReader r;
    private String current;
    /**
     * Creates a FileLineIterator for the reader. Fill out the constructor so
     * that a user can instantiate a FileLineIterator. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * The only method that should be called on BufferedReader is readLine() and
     * close(). You cannot call any other methods.
     *
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */
    public FileLineIterator(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("reader cannot be null");
        }
        this.r = reader;
        try {
            this.current = r.readLine();
        } catch (IOException e) {
            System.out.println("Unable to read line!");
        }

    }

    /**
     * Creates a FileLineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * <p>
     * DO NOT MODIFY THIS METHOD.
     *
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    /**
     * Takes in a filename and creates a BufferedReader.
     * See Java's documentation for BufferedReader to learn how to construct one
     * given a path to a file.
     *
     * @param filePath - the path to the CSV file to be turned to a
     *                 BufferedReader
     * @return a BufferedReader of the provided file contents
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public static BufferedReader fileToReader(String filePath) {
        BufferedReader reader;
        if (filePath == null) {
            throw new IllegalArgumentException("file is null!");
        }
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("This file doesn't exist!");
        }
        return reader;
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        boolean isNotNull = true;
        if (current == null) {
            isNotNull = false;
            try {
                r.close();
            } catch (IOException e) {
                System.out.println("Unable to close reader");
            }
        }
        return isNotNull;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        String now = "";
        if (this.hasNext()) {
            now = current;
            try {
                current = r.readLine();
            } catch (IOException e) {
                System.out.println("Unable to read the next line!");
            }
        } else {
            try {
                r.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to close the reader!");
            }
            throw new NoSuchElementException("No more lines");
        }
        return now;
    }
}

