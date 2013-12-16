package cz.edu.x3m;

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
        Globals.init ();
        Globals.getDatabase ().connect ();
        Globals.getController ().update ();
        startKnockServer ();
    }



    private synchronized void waitFor () throws InterruptedException {
        Thread serverThread = Globals.createServerThread ();
        serverThread.start ();
        serverThread.join ();
    }



    private void startKnockServer () throws Exception {
        while (true) {
            waitFor ();
            if (!Globals.getServer ().getConnection ().isValid ()) {
                System.out.println (Globals.getServer ().getConnection ().getErrorDetails ());
                break;
            }

            Globals.getController ().update ();
        }
    }
}
