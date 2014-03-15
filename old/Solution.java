package cz.edu.x3m.database.data;

import cz.edu.x3m.database.structure.AbstractDataObject;
import cz.edu.x3m.database.structure.AttemptObject;
import cz.edu.x3m.database.structure.TaskItem;
import cz.edu.x3m.database.structure.UserItem;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Solution extends AbstractDataObject implements ISolution {

    protected final int attemptID;
    protected final int userID;
    protected final int taskID;
    protected final int ordinal;
    protected final String language;
    protected TaskItem taskObject;
    protected UserItem userObject;
    protected AttemptObject attemptObject;



    public Solution (ResultSet row) throws SQLException {
        super (row);

        taskID = provider.getInt ("taskid");
        userID = provider.getInt ("userid");
        attemptID = provider.getInt ("attemptid");
        ordinal = provider.getInt ("ordinal");
        language = provider.getString ("language");
    }



    @Override
    public int getUserID () {
        return userID;
    }



    @Override
    public int getTaskID () {
        return taskID;
    }



    @Override
    public int getOrdinal () {
        return ordinal;
    }



    @Override
    public String getLanguage () {
        return language;
    }



    @Override
    public int getAttemptID () {
        return attemptID;
    }



    @Override
    public TaskItem getTaskObject () {
        return taskObject == null ? taskObject = provider.getTaskObject (taskID) : taskObject;
    }



    @Override
    public UserItem getUserObject () {
        return userObject == null ? userObject = provider.getUserObject (userID) : userObject;
    }



    @Override
    public AttemptObject getAttemptObject () {
        return attemptObject == null ? attemptObject = provider.getAttemptObject (attemptID) : attemptObject;
    }
}
