package cz.edu.x3m.core;

import cz.edu.x3m.controls.Controller;
import cz.edu.x3m.controls.IController;
import cz.edu.x3m.database.DatabaseFactory;
import cz.edu.x3m.database.IDatabase;
import cz.edu.x3m.net.KnockServerSocket;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Globals {

    private static IDatabase database;
    private static Config config;
    private static KnockServerSocket server;
    private static IController controller;



    public static IDatabase getDatabase () {
        return database;
    }



    public static Config getConfig () {
        return config;
    }



    public static KnockServerSocket getServer () {
        return server;
    }



    public static Thread createServerThread () {
        return new Thread (server);
    }



    public static IController getController () {
        return controller;
    }



    public static void init () throws Exception {
        Globals.config = Config.getInstance ();
        Globals.database = DatabaseFactory.getInstance (Globals.config.getType ());
        Globals.server = KnockServerSocket.getInstance ();
        Globals.controller = Controller.getInstance ();
    }
}
