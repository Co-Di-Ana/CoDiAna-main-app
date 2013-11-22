package cz.edu.x3m.grading.exception;

/**
 *
 * @author Jan Hybs
 */
public class GradingRuntimeException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>GradingRuntimeException</code> without detail message.
     */
    public GradingRuntimeException () {
    }



    /**
     * Constructs an instance of
     * <code>GradingRuntimeException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GradingRuntimeException (String msg) {
        super (msg);
    }



    /**
     * Constructs an instance of
     * <code>GradingRuntimeException</code> with the specified detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public GradingRuntimeException (String format, Object... args) {
        super (String.format (format, args));
    }
}
