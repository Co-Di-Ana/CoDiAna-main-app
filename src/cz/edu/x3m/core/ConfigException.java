package cz.edu.x3m.core;

/**
 *
 *  @author Jan Hybs
 */
public class ConfigException extends Exception {

    /**
     * Creates a new instance of <code>ConfigException</code> without detail message.
     */
    public ConfigException() {
    }


    /**
     * Constructs an instance of <code>ConfigException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConfigException(String msg) {
        super(msg);
    }
    
    


    /**
     * Constructs an instance of
     * <code>ConfigException</code> with the specified formatted detail message.
     *
     * @param format the detail message.
     * @param args parameters
     */
    public ConfigException (String format, Object... args) {
        super (String.format (format, args));
    }
}
