package cz.edu.x3m.controls;

import cz.edu.x3m.database.data.PlagsCheckStateType;
import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.data.types.QueueItemType;
import java.sql.Timestamp;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class UpdateResult {

    private final Timestamp timesent;
    private final QueueItemType type;
    private final String user;
    private String language;
    private AttemptStateType attemptState;
    private PlagsCheckStateType plagState;



    public UpdateResult (QueueItem queueItem, AttemptStateType attemptStateType) {
        user = queueItem.getUserItem ().getFullname ();
        language = queueItem.getAttemptItem ().getLanguage ();
        timesent = queueItem.getAttemptItem ().getTimeSent ();
        attemptState = attemptStateType;
        type = queueItem.getType ();
    }



    public UpdateResult (QueueItem queueItem, PlagsCheckStateType attemptStateType) {
        user = queueItem.getUserID () == 0 ? "EVERYONE" : queueItem.getUserItem ().getFullname ();
        timesent = new Timestamp (System.currentTimeMillis ());
        plagState = attemptStateType;
        type = queueItem.getType ();
    }



    private UpdateResult (String user, String language, Timestamp timesent, AttemptStateType attemptExit, QueueItemType type) {
        this.user = user;
        this.language = language;
        this.timesent = timesent;
        this.attemptState = attemptExit;
        this.type = type;
    }



    @Override
    public String toString () {
        switch (type) {
            case TYPE_MEASURE_VALUES:
            case TYPE_SOLUTION_CHECK:
                return String.format ("%s %s %s %s",
                                      timesent,
                                      String.format ("[%s]", type),
                                      user,
                                      String.format ("[%s]", attemptState));
            case TYPE_PLAGIARISM_CHECK:
                return String.format ("%s %s %s %s",
                                      timesent,
                                      String.format ("[%s]", type),
                                      user,
                                      String.format ("[%s]", plagState));
            default:
            case TYPE_UNKNOWN:
                return null;
        }
    }
}
