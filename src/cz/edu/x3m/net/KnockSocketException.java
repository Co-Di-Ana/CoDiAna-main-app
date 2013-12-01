package cz.edu.x3m.net;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class KnockSocketException extends Exception {

    /**
     * Creates a new instance of
     * <code>KnockSocketException</code> without detail message.
     */
    public KnockSocketException () {
    }



    /**
     * Constructs an instance of
     * <code>KnockSocketException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public KnockSocketException (String msg) {
        super (msg);
    }



    /**
     * Constructs an instance of
     * <code>KnockSocketException</code> with the specified formatted detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public KnockSocketException (String format, Object... args) {
        super (String.format (format, args));
    }


    /**
     * Constructs an instance of
     * <code>KnockSocketException</code> with the throwable object
     *
     * @param cause throwable object
     */
    public KnockSocketException (Throwable cause) {
        super (cause);
    }
    
    
}
