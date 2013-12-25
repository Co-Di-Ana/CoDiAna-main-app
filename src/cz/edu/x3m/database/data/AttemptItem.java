package cz.edu.x3m.database.data;

import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.execution.IExecutionResult;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jan Hybs
 */
public class AttemptItem extends AbstractDetailItem {

    private int id;
    private int ordinal;
    private AttemptStateType state;
    private String language;
    private String firstname;
    private String lastname;
    private boolean isComplex;
    private TaskItem taskItem;



    public AttemptItem (int taskID, int relatedID, ResultSet row) throws SQLException, Exception {
        super (taskID, relatedID);
        id = row.getInt ("id");
        ordinal = row.getInt ("ordinal");
        state = AttemptStateType.create (row.getInt ("state"));
        language = row.getString ("language");
        firstname = row.getString ("firstname");
        lastname = row.getString ("lastname");
        isComplex = row.getInt ("detail") == 1;
    }



    /**
     * @return the unique rown attempt id
     */
    public int getId () {
        return id;
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
     * @return the firstname
     */
    public String getFirstname () {
        return firstname;
    }



    /**
     * @return the lastname
     */
    public String getLastname () {
        return lastname;
    }



    /**
     * @return the full name LASTNAME firstname
     */
    public String getFullname () {
        return String.format ("%s %s", lastname.toUpperCase (), firstname);
    }



    /**
     * @return the isComplex whether is solution zipped
     */
    public boolean isComplex () {
        return isComplex;
    }



    /**
     *
     * @param taskItem
     */
    public void setTaskItem (TaskItem taskItem) {
        this.taskItem = taskItem;
    }



    /**
     * @return the taskItem
     */
    public TaskItem getTaskItem () {
        return taskItem;
    }



    public void setCompilationError (ICompileResult result) {
    }



    public void setExecutionError (IExecutionResult result) {
    }
}
