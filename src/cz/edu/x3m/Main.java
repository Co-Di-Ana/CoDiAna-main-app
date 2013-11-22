package cz.edu.x3m;

import cz.edu.x3m.core.Config;
import cz.edu.x3m.core.Globals;

/**
 * @author Jan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) throws Exception {
        new Main ();
    }



    /**
     * Creates new instance of Main class
     */
    public Main () throws Exception {
        Globals.init (Config.loadConfig ());

        Globals.getDatabase ().connect ();
        Globals.getDatabase ().close ();
    }
}
