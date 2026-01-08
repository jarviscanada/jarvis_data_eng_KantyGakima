package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

    /**
     *
     * @throws IOException
     */
    void process() throws IOException;

    /**
     * Traverse a given directory recursively and return all files
     * @param rootDir input directory
     * @return files under the rootDir
     */
    List<File> listFiles(String rootDir);

    /**
     * Read all lines from a file and return them as a list
     *
     * @param inputFile
     * @return lines
     * @throws IllegalArgumentException if a given inputFile is not a file
     */
    List<String> readLines (File inputFile) throws IOException;

    /**
     * Check if a line contains the regex pattern (passed by user)
     * @param line
     * @return true is there is a match
     */
    boolean containsPattern(String line);

    /**
     * Write lines to a file
     * Explore: FileOutputStream, outputStreamWriter, and BufferedWriter
     *
     * @param lines matched line
     * @throws IOException if write failed
     */
    void writeToFile(List<String> lines) throws IOException;

    //Getters and Setters
    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);
}
