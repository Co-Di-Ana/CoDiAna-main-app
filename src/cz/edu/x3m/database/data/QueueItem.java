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
    private final String type;
    private final int relatedID;
    private final int taskID;
    private final int id;



    public QueueItem (ResultSet row) throws SQLException, DatabaseException {
        taskItem = new TaskItem (row);
        id = row.getInt ("id");
        taskID = row.getInt ("taskid");
        relatedID = row.getInt ("relatedid");

        String tmp = row.getString ("type");
        type = tmp.equalsIgnoreCase ("solution") ? TYPE_SOLUTION_CHECK
               : tmp.equalsIgnoreCase ("plagiarism") ? TYPE_PLAGIARISM_CHECK : null;

        //# unsupported type
        if (type == null)
            throw new DatabaseException ("Unknown queue type '%s'", tmp);
    }



    public void loadDetails () throws DatabaseException {
        if (TYPE_SOLUTION_CHECK.equalsIgnoreCase (type))
            detailItem = Globals.getDatabase ().getSolutionCheckItem (taskID, relatedID);
        else if (TYPE_PLAGIARISM_CHECK.equalsIgnoreCase (type))
            detailItem = Globals.getDatabase ().getPlagiarismCheckItem (taskID, relatedID);
        else
            throw new DatabaseException ("Unknown queue type '%s'", type);

    }



    /**
     * @return the task item
     */
    public TaskItem getTaskItem () {
        return taskItem;
    }



    /**
     * @return the type
     */
    public String getType () {
        return type;
    }



    /**
     * @return the detail item
     */
    public AbstractDetailItem getDetailItem () {
        return detailItem;
    }



    /**
     * @return the row unique id
     */
    public int getId () {
        return id;
    }
}
