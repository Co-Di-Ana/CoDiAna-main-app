package cz.edu.x3m.utils;

import cz.edu.x3m.core.Globals;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Strings {

    private Strings () {
    }



    public static String create (Object... args) {
        String format = "";
        for (int i = 0; i < args.length; i++)
            format += "%s%n";

        return String.format (format, args);
    }



    public static String createAndReplace (Object... args) {
        return replaceDBPrefix (create (args));
    }



    public static String replaceDBPrefix (String sql) {
        String prefix = Globals.getConfig ().getPrefix ();
        return sql.replaceAll ("::", prefix);
    }
}
