package cz.edu.x3m.plagiarism.exception;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class CompareException extends Exception {

    public CompareException (String message) {
        super (message);
    }



    public CompareException (Throwable cause) {
        super (cause);
    }
}
