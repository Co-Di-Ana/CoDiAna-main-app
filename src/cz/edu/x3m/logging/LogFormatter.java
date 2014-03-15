package cz.edu.x3m.logging;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the {@code LogRecord} in a human readable
 * format.  The summary will typically be 1 or 2 lines.
 *
 * <p>
 * <a name="formatting">
 * <b>Configuration:</b></a>
 * The {@code SimpleFormatter} is initialized with the
 * <a href="../Formatter.html#syntax">format string</a>
 * specified in the {@code java.util.logging.SimpleFormatter.format}
 * property to {@linkplain #format format} the log messages.
 * This property can be defined
 * in the {@linkplain LogManager#getProperty logging properties}
 * configuration file
 * or as a system property.  If this property is set in both
 * the logging properties and system properties,
 * the format string specified in the system property will be used.
 * If this property is not defined or the given format string
 * is {@linkplain java.util.IllegalFormatException illegal},
 * the default format is implementation-specific.
 *
 * @since 1.4
 * @see java.util.Formatter
 */
public class LogFormatter extends Formatter {

    // format string for printing the log record
    private final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    private final Date dat = new Date ();



    @Override
    public synchronized String format (LogRecord record) {
        dat.setTime (record.getMillis ());
        String source;
        if (record.getSourceClassName () != null) {
            source = record.getSourceClassName ();
            if (record.getSourceMethodName () != null) {
                source += " " + record.getSourceMethodName ();
            }
        } else {
            source = record.getLoggerName ();
        }
        String message = formatMessage (record);
        String throwable = "";
        if (record.getThrown () != null) {
            StringWriter sw = new StringWriter ();
            PrintWriter pw = new PrintWriter (sw);
            pw.println ();
            record.getThrown ().printStackTrace (pw);
            pw.close ();
            throwable = sw.toString ();
        }

        String log;
        if (throwable == null || throwable.isEmpty ())
            log = String.format ("%s %-10s %s%n", dateFormat.format (dat), record.getLevel (), message);
        else
            log = String.format ("%s %-10s %s%n%s%n", dateFormat.format (dat), record.getLevel (), message, throwable);

        System.out.print (log);
        return log;
    }
}
