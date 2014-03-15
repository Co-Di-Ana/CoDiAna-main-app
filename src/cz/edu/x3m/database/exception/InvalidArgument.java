package cz.edu.x3m.database.exception;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class InvalidArgument extends Exception {

    public InvalidArgument (String invalid_value) {
        super (invalid_value);
    }
}
