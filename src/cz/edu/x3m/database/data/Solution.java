package cz.edu.x3m.database.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Solution implements ISolution {

    protected final int attemptID;
    protected final int userID;
    protected final int taskID;
    protected final int ordinal;
    private final String language;



    public Solution (ResultSet row) throws SQLException {
        taskID = row.getInt ("taskid");
        userID = row.getInt ("userid");
        attemptID = row.getInt ("attemptid");
        ordinal = row.getInt ("ordinal");
        language = row.getString ("language");
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
    
    
}
