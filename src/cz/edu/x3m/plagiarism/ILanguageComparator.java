package cz.edu.x3m.plagiarism;

import java.io.File;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface ILanguageComparator {

    /**
     * Method prepares one solution for repeated use.
     * @param dirA directory where all files are stored in tree file structure
     * @throws Exception when error occures while parsing, etc
     */
    void prepare (File dirA) throws Exception;



    /**
     * Method compares this solution to prepared one. If there is no prepared solution, error is thrown.
     * @param directory where all files are stored in tree file structure
     * @return Difference between 2 solutions
     * @throws Exception when error occures while parsing, etc
     */
    Difference compare (File dirB) throws Exception;



    /**
     * Method compares two solutions.
     * @param dirA directory where all files are stored in tree file structure
     * @param dirA second directory where all files are stored in tree file structure
     * @return Difference between 2 solutions
     * @throws Exception when error occures while parsing, etc
     */
    Difference compare (File dirA, File dirB) throws Exception;
}
