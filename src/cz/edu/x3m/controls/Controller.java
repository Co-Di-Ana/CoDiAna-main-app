package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.QueueItem;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Controller implements IController {

    private static IController instance;



    public static IController getInstance () {
        return instance == null ? instance = new Controller () : instance;
    }



    @Override
    public synchronized UpdateResult update () throws Exception {
        List<QueueItem> items = Globals.getDatabase ().getItems ();
        
        if (items == null || items.isEmpty ())
            return new UpdateResult (UpdateResultType.NO_ITEMS);
        System.out.println (items);
        return null;
    }
}
