package cz.edu.x3m.database;

/**
 *
 * @author Jan Hybs
 */
public class DatabaseFactory {

    public static final String TYPE_LOCAL = "local";
    public static final String TYPE_REMOTE = "remote";



    public static IDatabase getInstance (String name) {
        if (name.equalsIgnoreCase (TYPE_LOCAL))
            return new LocalDatabase ();

        return null;
    }
}
