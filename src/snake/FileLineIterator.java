package snake;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;


//FileLineIterator: useful wrapper around Java's BufferedReader. Reads line whenever the next() method is called.
public class FileLineIterator implements Iterator<String> {

    // Fields needed to implement your FileLineIterator
    private BufferedReader reader;
    private String nextLine;


    //Creates a FileLineIterator for the reader. User can instantiate a FileLineIterator with constructor.
    public FileLineIterator(BufferedReader reader) {

        try {
            this.reader = reader;
            this.nextLine = reader.readLine();
        } catch (NullPointerException n) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            this.nextLine = null;
        }
    }

    // Creates a FileLineIterator from a provided filePath by creating FileReader and BufferedReader for the file
    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    //Takes in a filename and creates a BufferedReader.
    public static BufferedReader fileToReader(String filePath) {

        if (filePath == null) {
            throw new IllegalArgumentException();
        }

        try {
            return new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }

    }

    // Returns true if there are lines left to read in the file, and false otherwise.

    @Override
    public boolean hasNext() {

        if (nextLine == null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Error encountered while closing file");
            }
        }
        return nextLine != null;
    }


    // Returns the next line from the file, or throws a NoSuchElementException if there are no more strings left to return (i.e. hasNext() is false).

    @Override
    public String next() {

        String line = nextLine;

        if (hasNext()) {
            try {
                nextLine = reader.readLine();
            } catch (IOException e) {
                nextLine = null;
            }
        } else {
            throw new NoSuchElementException();
        }
        return line;

    }
}
