package cz.edu.x3m;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.logging.Log;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PeriodicUpdater extends TimerTask {

    private static final Timer TIMER = new Timer ("update timer");
    private static final PeriodicUpdater INSTANCE = new PeriodicUpdater ();
    private int period;



    private PeriodicUpdater () {
    }



    public static void start (int periodMs) {
        Log.info ("Starting scheduled timer");
        INSTANCE.period = periodMs;
        TIMER.schedule (INSTANCE, 0, periodMs);
    }



    @Override
    public void run () {
        Log.info ("updating (scheduled task)");
        try {
            Globals.getController ().update ();
        } catch (Exception ex) {
            Log.err (ex);
        }
        Log.info ("waiting %d ms", period);
    }
}
