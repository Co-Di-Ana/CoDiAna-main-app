package cz.edu.x3m.database.exception;

/**
 *
 *  @author Jan Hybs
 */
public class DatabaseException extends Exception {

    /**
     * Creates a new instance of <code>DatabaseException</code> without detail message.
     */
    public DatabaseException() {
    }


    /**
     * Constructs an instance of <code>DatabaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DatabaseException(String msg) {
        super(msg);
    }
    
    


    /**
     * Constructs an instance of
     * <code>DatabaseException</code> with the specified detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public DatabaseException (String format, Object... args) {
        super (String.format (format, args));
    }


    /**
     * Constructs an instance of <code>DatabaseException</code> based on Throwable exception
     * @param msg Throwable exception
     */
    public DatabaseException (Throwable cause) {
        super (cause);
    }
    
    
}
