package cz.edu.x3m.database.data;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.processing.execution.IExecutionResult;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Hybs
 */
public class QueueItem {

    private final TaskItem taskItem;
    private AbstractDetailItem detailItem;
    private final QueueType type;
    private final int id;
    private final int userID;
    private final int taskID;
    private final int attemptID;
    private final int priority;
    private IExecutionResult executionResult;



    public QueueItem (ResultSet row) throws SQLException, DatabaseException {
        taskItem = new TaskItem (row);
        id = row.getInt ("id");
        taskID = row.getInt ("taskid");
        userID = row.getInt ("userid");
        attemptID = row.getInt ("attemptid");
        type = QueueType.getByValue (row.getInt ("type"));
        priority = row.getInt ("priority");


        //# unsupported type
        if (type == QueueType.TYPE_UNKNOWN)
            throw new DatabaseException ("Unknown queue item type");
    }



    public void loadDetails () throws DatabaseException {
        switch (getType ()) {
            case TYPE_SOLUTION_CHECK:
            case TYPE_MEASURE_VALUES:
                detailItem = Globals.getDatabase ().getSolutionCheckItem (getTaskID (), getUserID ());
                break;
            case TYPE_PLAGIARISM_CHECK:
                detailItem = Globals.getDatabase ().getPlagiarismCheckItem (getTaskID (), getUserID ());
                break;
            case TYPE_UNKNOWN:
                throw new DatabaseException ("Unknown queue type '%s'", getType ());
            default:
        }

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
        TYPE_MEASURE_VALUES,
        TYPE_UNKNOWN;



        public static QueueType getByValue (int value) {
            switch (value) {
                case 1:
                    return TYPE_SOLUTION_CHECK;
                case 2:
                    return TYPE_MEASURE_VALUES;
                case 3:
                    return TYPE_PLAGIARISM_CHECK;
                default:
                    return TYPE_UNKNOWN;
            }
        }
    }



    /**
     * @return the execution result
     */
    public IExecutionResult getExecutionResult () {
        return executionResult;
    }



    /**
     * @param executionResult the execution result to set
     */
    public void setExecutionResult (IExecutionResult executionResult) {
        this.executionResult = executionResult;
    }
}
