package cz.edu.x3m.core;

import cz.edu.x3m.database.DatabaseFactory;
import cz.edu.x3m.database.IDatabase;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Globals {

    private static IDatabase database;
    private static Config config;



    public static IDatabase getDatabase () {
        return database;
    }



    public static Config getConfig () {
        return config;
    }



    public static void init (Config config) {
        Globals.config = config;
        Globals.database = DatabaseFactory.getInstance (Globals.config.getType ());
    }
}
