package cz.edu.x3m.grading.exception;

/**
 *
 * @author Jan Hybs
 */
public class GradingException extends Exception {

    /**
     * Creates a new instance of
     * <code>GradingException</code> without detail message.
     */
    public GradingException () {
    }



    /**
     * Constructs an instance of
     * <code>GradingException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GradingException (String msg) {
        super (msg);
    }



    /**
     * Constructs an instance of
     * <code>GradingException</code> with the specified detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public GradingException (String format, Object... args) {
        super (String.format (format, args));
    }
}
