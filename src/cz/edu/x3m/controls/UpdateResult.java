package cz.edu.x3m.controls;

import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.data.types.QueueItemType;
import java.sql.Timestamp;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class UpdateResult {

    private final String user;
    private final String language;
    private final Timestamp timesent;
    private final AttemptStateType attemptExit;
    private final QueueItemType type;



    public UpdateResult (QueueItem queueItem, AttemptItem attemptItem, AttemptStateType attemptStateType) {
        user = attemptItem.getFullname ();
        language = attemptItem.getLanguage ();
        timesent = attemptItem.getTimeSent ();
        attemptExit = attemptStateType;
        type = queueItem.getType ();
    }



    public UpdateResult (String user, String language, Timestamp timesent, AttemptStateType attemptExit, QueueItemType type) {
        this.user = user;
        this.language = language;
        this.timesent = timesent;
        this.attemptExit = attemptExit;
        this.type = type;
    }



    @Override
    public String toString () {
        return String.format ("%-32s %s %s %32s",
                              timesent,
                              StringUtils.center (String.format ("[%s]", type), 32),
                              StringUtils.center (user, 32),
                              String.format ("[%s]", attemptExit));
    }
}
