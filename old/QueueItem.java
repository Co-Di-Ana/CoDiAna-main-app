package cz.edu.x3m.database.data;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.types.QueueItemType;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.database.structure.TaskObject;
import cz.edu.x3m.processing.execution.IExecutionResult;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Hybs
 */
public class QueueItem extends Solution implements IQueueItem {

    private final TaskObject taskItem;
    private AbstractDetailItem detailItem;
    private final QueueItemType type;
    private final int id;
    private final int priority;
    private IExecutionResult executionResult;



    public QueueItem (ResultSet row) throws SQLException, DatabaseException, InvalidArgument {
        super (row);
        
        taskItem = new TaskObject (row);
        id = row.getInt ("queueid");
        type = QueueItemType.create (row.getInt ("type"));
        priority = row.getInt ("priority");


        //# unsupported type
        if (type == QueueItemType.TYPE_UNKNOWN)
            throw new DatabaseException ("Unknown queue item type");
    }



    public void loadDetails () throws DatabaseException {
        switch (getType ()) {
            case TYPE_SOLUTION_CHECK:
            case TYPE_MEASURE_VALUES:
                detailItem = Globals.getDatabase ().getSolutionCheckItem (getTaskID (), getUserID ());
                break;
            case TYPE_PLAGIARISM_CHECK:
                detailItem = Globals.getDatabase ().getPlagiarismCheckItem (getTaskID (), getUserID (), getTaskItem ().getGradeMethod ());
                break;
            case TYPE_UNKNOWN:
                throw new DatabaseException ("Unknown queue type '%s'", getType ());
            default:
        }

    }



    /**
     * @return the task item
     */
    public TaskObject getTaskItem () {
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
    public QueueItemType getType () {
        return type;
    }



    /**
     * @return the unique queue id
     */
    public int getId () {
        return id;
    }



    /**
     * @return the priority in queue
     */
    public int getPriority () {
        return priority;
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
