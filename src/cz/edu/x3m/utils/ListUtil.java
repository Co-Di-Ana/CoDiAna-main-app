package cz.edu.x3m.utils;

import cz.edu.x3m.database.structure.AttemptItem;
import java.util.List;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class ListUtil {

    public static int findByAttemptID (List<AttemptItem> solutions, int attemptID) {
        if (solutions == null || solutions.isEmpty ())
            return -1;

        for (int i = 0, j = solutions.size (); i < j; i++)
            if (solutions.get (i).getId () == attemptID)
                return i;

        return -1;
    }
}
