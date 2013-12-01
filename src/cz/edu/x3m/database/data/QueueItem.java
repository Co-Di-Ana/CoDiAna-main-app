package cz.edu.x3m.database.data;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.exception.DatabaseException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Hybs
 */
public class QueueItem {

    public static final String TYPE_SOLUTION_CHECK = "solutionCheck";
    public static final String TYPE_PLAGIARISM_CHECK = "plagiarismCheck";
    private final TaskItem taskItem;
    private AbstractDetailItem detailItem;
    private final QueueType type;
    private final int id;
    private final int userID;
    private final int taskID;
    private final int attemptID;
    private final int priority;



    public QueueItem (ResultSet row) throws SQLException, DatabaseException {
        taskItem = new TaskItem (row);
        id = row.getInt ("id");
        taskID = row.getInt ("taskid");
        userID = row.getInt ("userid");
        attemptID = row.getInt ("attemptid");
        int tmp = row.getInt ("type");
        priority = row.getInt ("priority");

        type = tmp == 0 ? QueueType.TYPE_SOLUTION_CHECK
               : tmp == 1 ? QueueType.TYPE_PLAGIARISM_CHECK
                 : QueueType.TYPE_UNKNOWN;

        //# unsupported type
        if (type == QueueType.TYPE_UNKNOWN)
            throw new DatabaseException ("Unknown queue type '%s'", tmp);
    }



    public void loadDetails () throws DatabaseException {
        if (getType () == QueueType.TYPE_SOLUTION_CHECK)
            detailItem = Globals.getDatabase ().getSolutionCheckItem (getTaskID (), getUserID ());
        else if (getType () == QueueType.TYPE_PLAGIARISM_CHECK)
            detailItem = Globals.getDatabase ().getPlagiarismCheckItem (getTaskID (), getUserID ());
        else
            throw new DatabaseException ("Unknown queue type '%s'", getType ());

    }



    /**
     * @return the task item
     */
    public TaskItem getTaskItem () {
        return taskItem;
    }



    /**
     * @return the detailItem
     */
    public AbstractDetailItem getDetailItem () {
        return detailItem;
    }



    /**
     * @return the type of queue (solution/plagiarism)
     */
    public QueueType getType () {
        return type;
    }



    /**
     * @return the unique queue id
     */
    public int getId () {
        return id;
    }



    /**
     * @return the user ID
     */
    public int getUserID () {
        return userID;
    }



    /**
     * @return the task ID
     */
    public int getTaskID () {
        return taskID;
    }



    /**
     * @return the attempt ID if exists or 0 if not
     */
    public int getAttemptID () {
        return attemptID;
    }



    /**
     * @return the priority in queue
     */
    public int getPriority () {
        return priority;
    }

    public static enum QueueType {

        TYPE_SOLUTION_CHECK,
        TYPE_PLAGIARISM_CHECK,
        TYPE_UNKNOWN
    }
}
