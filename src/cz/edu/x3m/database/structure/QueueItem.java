package cz.edu.x3m.database.structure;

import cz.edu.x3m.database.exception.InvalidArgument;
import cz.edu.x3m.database.data.types.QueueItemType;
import cz.edu.x3m.processing.execution.IExecutionResult;
import java.sql.ResultSet;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class QueueItem extends AbstractDataObject {

    private final int id, taskID, userID, attemptID, priority;
    private final QueueItemType type;
    //---
    private TaskItem taskObject;
    private UserItem userObject;
    private PlagItem plagObject;
    private AttemptItem attemptObject;



    public QueueItem (ResultSet row) throws InvalidArgument {
        super (row);

        id = provider.getInt ("id");
        taskID = provider.getInt ("taskid");
        userID = provider.getInt ("userid");
        attemptID = provider.getInt ("attemptid");
        priority = provider.getInt ("priority");
        type = QueueItemType.create (provider.getInt ("type"));
    }



    public QueueItemType getType () {
        return type;
    }



    public int getId () {
        return id;
    }



    public int getUserID () {
        return userID;
    }



    public int getTaskID () {
        return taskID;
    }



    public int getAttemptID () {
        return attemptID;
    }



    public int getPriority () {
        return priority;
    }



    public TaskItem getTaskItem () {
        return taskObject == null ? taskObject = provider.getTaskItem (taskID) : taskObject;
    }



    public UserItem getUserItem () {
        return userObject == null ? userObject = provider.getUserItem (userID) : userObject;
    }



    public AttemptItem getAttemptItem () {
        return attemptObject == null ? attemptObject = provider.getAttemptItem (attemptID) : attemptObject;
    }



    public PlagItem getPlagObject () {
        return plagObject == null
               ? plagObject = provider.getPlagItem (getTaskID (), getUserID (), getTaskItem ().getGradeMethod ())
               : plagObject;
    }
}
