package cz.edu.x3m.database.structure;

import cz.edu.x3m.database.exception.InvalidArgument;
import cz.edu.x3m.database.data.PlagsCheckStateType;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.processing.IRunEvaluation;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.execution.IExecutionResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author Jan Hybs
 */
public class AttemptItem extends AbstractDataObject {

    private final int id, taskID, userID, ordinal;
    private TaskItem taskObject;
    private UserItem userObject;
    private final AttemptStateType state;
    private final PlagsCheckStateType plagsState;
    private final String language;
    private final boolean isComplex;
    private final Timestamp timeSent;
    //
    private IRunEvaluation result;



    public AttemptItem (ResultSet row) throws SQLException, InvalidArgument {
        super (row);

        id = row.getInt ("id");
        taskID = provider.getInt ("taskid");
        userID = provider.getInt ("userid");
        plagsState = PlagsCheckStateType.create (provider.getInt ("plagscheckstate"));
        ordinal = row.getInt ("ordinal");
        state = AttemptStateType.create (row.getInt ("state"));
        language = row.getString ("language");
        isComplex = row.getInt ("detail") == 1;
        timeSent = new Timestamp (row.getLong ("timesent"));
    }



    /**
     * @return the unique row attempt id
     */
    public int getId () {
        return id;
    }



    public int getTaskID () {
        return taskID;
    }



    public int getUserID () {
        return userID;
    }



    /**
     * @return the ordinal (serial number) of this attempt
     */
    public int getOrdinal () {
        return ordinal;
    }



    /**
     * @return the attempt state
     */
    public AttemptStateType getState () {
        return state;
    }



    /**
     * @return the language used in this attempt
     */
    public String getLanguage () {
        return language;
    }



    /**
     * @return the isComplex whether is solution zipped
     */
    public boolean isComplex () {
        return isComplex;
    }



    /**
     * @return the timeSent
     */
    public Timestamp getTimeSent () {
        return timeSent;
    }



    public TaskItem getTaskItem () {
        return taskObject == null ? taskObject = provider.getTaskItem (taskID) : taskObject;
    }



    public UserItem getUserItem () {
        return userObject == null ? userObject = provider.getUserItem (userID) : userObject;
    }



    public void setCompilationError (ICompileResult result) {
    }



    public void setExecutionError (IExecutionResult result) {
    }



    public IRunEvaluation getExecutionResult () {
        return result;
    }



    public void setExecutionResult (IRunEvaluation result) {
        this.result = result;
    }
}
