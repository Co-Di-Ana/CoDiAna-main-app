package cz.edu.x3m.processing.execption;

/**
 *
 * @author Jan Hybs
 */
public class ExecutionException extends Exception {

    /**
     * Creates a new instance of
     * <code>ExecutionException</code> without detail message.
     */
    public ExecutionException () {
    }



    /**
     * Constructs an instance of
     * <code>ExecutionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExecutionException (String msg) {
        super (msg);
    }


    /**
     * Constructs an instance of
     * <code>ExecutionException</code> Throwable execption
     *
     * @param cause Throwable exception
     */
    public ExecutionException (Throwable cause) {
        super (cause);
    }

    


    /**
     * Constructs an instance of
     * <code>ExecutionException</code> with the specified detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public ExecutionException (String format, Object... args) {
        super (String.format (format, args));
    }
}
