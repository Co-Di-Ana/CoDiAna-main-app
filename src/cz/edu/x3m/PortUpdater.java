package cz.edu.x3m;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.logging.Log;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PortUpdater implements Runnable {

    private static final PortUpdater INSTANCE = new PortUpdater ();
    private static final Thread PORT_THREAD = new Thread (INSTANCE);



    public static void start () {
        Log.info ("starting port thread");
        PORT_THREAD.start ();
    }



    @Override
    public void run () {
        while (true) {
            try {
                waitFor ();
            } catch (InterruptedException ex) {
                Log.err (ex);
            }
            if (!Globals.getServer ().getConnection ().isValid ()) {
                Log.err (Globals.getServer ().getConnection ().getErrorDetails ());
                break;
            }
            Log.info ("updating (port knock)");
            try {
                Globals.getController ().update ();
            } catch (Exception ex) {
                Log.err (ex);
            }
        }
    }



    private synchronized void waitFor () throws InterruptedException {
        Thread serverThread = Globals.createServerThread ();
        serverThread.start ();
        serverThread.join ();
    }
}
