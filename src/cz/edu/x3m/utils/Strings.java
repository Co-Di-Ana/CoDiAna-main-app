package cz.edu.x3m.utils;

import cz.edu.x3m.core.Globals;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Strings {

    public static String empty (String check, String defaultValue) {
        return check == null || isOnlyWhitespace (check) ? defaultValue : check;
    }



    public static String empty (String check) {
        return empty (check, "");
    }



    private Strings () {
    }



    /**
     * Creates joined string lines
     *
     * @param lines query lines
     * @return sql query with unreplaced prefixes
     */
    public static String create (Object... lines) {
        String format = "";
        for (int i = 0; i < lines.length; i++)
            format += "%s%n";

        return String.format (format, lines);
    }



    /**
     * Creates joined string lines and replaces all prefixes '::' with config database prefix
     *
     * @param lines query lines
     * @return sql query
     */
    public static String createAndReplace (Object... lines) {
        return replaceDBPrefix (create (lines));
    }



    /**
     * Replaces all prefixes '::' with config database prefix
     *
     * @param sql query
     * @return sql query
     */
    public static String replaceDBPrefix (String sql) {
        String prefix = Globals.getConfig ().getPrefix ();
        return sql.replaceAll ("::", prefix);
    }



    /**
     * Method compares given string to and whitespace chars
     *
     * @param value
     * @return true if string si made of whitespace
     */
    public static boolean isOnlyWhitespace (String value) {
        if (value == null)
            return true;

        int l = value.length ();
        char c;

        if (l == 0)
            return true;
        for (int i = 0; i < l; i++) {
            c = value.charAt (i);
            if (!(c == '\n' || c == '\t' || c == '\r' || c == ' '))
                return false;
        }

        return true;
    }
}
