package cz.edu.x3m;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.logging.Log;


// ------- EMPTY LIMIT TIME, LIMIT MEMORY atd --------------------------------------------
/**
 * @author Jan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        try {
            new Main ();
        } catch (Exception ex) {
            Log.err (ex);
        }
    }



    /**
     * Creates new instance of Main class
     */
    public Main () throws Exception {
        Log.init ();
        Log.info ("APPLICATION START");
        
        Globals.init ();
        Log.info ("connecting to DB");
        Globals.getDatabase ().connect ();
        
        Log.info ("starting threads");
        PeriodicUpdater.start (5 * 60 * 1000);
        PortUpdater.start ();
    }
}
