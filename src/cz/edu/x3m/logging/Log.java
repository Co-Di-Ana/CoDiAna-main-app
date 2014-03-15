package cz.edu.x3m.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Log {

    private static final Logger logger = Logger.getLogger ("CoDiAna");
    private static boolean inited;



    public static void info (String message, Object... args) {
        log (Level.INFO, String.format (message, args));
    }



    public static void info (String message) {
        log (Level.INFO, message);
    }



    public static void log (Level logLevel, String message) {
        if (message != null && !message.isEmpty ())
            logger.log (logLevel, message);
    }



    public static void init () {
        if (inited)
            return;

        logger.setUseParentHandlers (false);

        try {
            FileHandler handler = new FileHandler ("./log.txt", true);
            handler.setFormatter (new LogFormatter ());
            logger.addHandler (handler);
        } catch (IOException | SecurityException e) {
            System.err.println ("Cannot create log file handler!");
            e.printStackTrace ();
        }

        info ("-----------------------------------------------------");

        inited = true;
    }



    public static void err (String errorDetails) {
        log (Level.SEVERE, errorDetails);
    }



    public static void err (String message, Object... args) {
        log (Level.SEVERE, String.format (message, args));
    }



    public static void err (Throwable ex) {
        if (ex != null)
            logger.log (Level.SEVERE, ex.getLocalizedMessage (), ex);
    }
}
