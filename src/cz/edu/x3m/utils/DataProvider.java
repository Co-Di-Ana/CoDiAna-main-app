package cz.edu.x3m.utils;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.GradeMethod;
import cz.edu.x3m.database.structure.PlagItem;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.database.structure.AttemptItem;
import cz.edu.x3m.database.structure.TaskItem;
import cz.edu.x3m.database.structure.UserItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class DataProvider {

    private final ResultSet row;
    private static final Map<Integer, TaskItem> tasks = new HashMap<> ();
    private static final Map<Integer, UserItem> users = new HashMap<> ();
    private static final Map<Integer, AttemptItem> attempts = new HashMap<> ();



    public static void clearCache () {
        tasks.clear ();
        users.clear ();
        attempts.clear ();
    }



    public DataProvider (ResultSet resultSet) {
        this.row = resultSet;
    }



    public int getInt (String id) {
        return getInt (id, 0);
    }



    public int getInt (String id, int defaultValue) {
        try {
            return row.getInt (id);
        } catch (SQLException ex) {
            System.out.format ("ERROR: %s%n", ex.getMessage ());
            ex.printStackTrace ();
            return defaultValue;
        }
    }



    public long getLong (String id) {
        return getLong (id, 0);
    }



    public long getLong (String id, long defaultValue) {
        try {
            return row.getLong (id);
        } catch (SQLException ex) {
            System.out.format ("ERROR: %s%n", ex.getMessage ());
            ex.printStackTrace ();
            return defaultValue;
        }
    }



    public String getString (String id) {
        return getString (id, null);
    }



    public String getString (String id, String defaultValue) {
        try {
            return row.getString (id);
        } catch (SQLException ex) {
            System.out.format ("ERROR: %s%n", ex.getMessage ());
            ex.printStackTrace ();
            return defaultValue;
        }
    }



    public UserItem getUserItem (int id) {
        try {
            if (users.containsKey (id))
                return users.get (id);
            users.put (id, Globals.getDatabase ().getUserItem (id));
            return users.get (id);
        } catch (DatabaseException ex) {
            return null;
        }
    }



    public TaskItem getTaskItem (int id) {
        try {
            if (tasks.containsKey (id))
                return tasks.get (id);
            tasks.put (id, Globals.getDatabase ().getTaskItem (id));
            return tasks.get (id);
        } catch (DatabaseException ex) {
            return null;
        }
    }



    public AttemptItem getAttemptItem (int id) {
        try {
            if (attempts.containsKey (id))
                return attempts.get (id);
            attempts.put (id, Globals.getDatabase ().getAttemptItem (id));
            return attempts.get (id);
        } catch (DatabaseException ex) {
            return null;
        }
    }



    public PlagItem getPlagItem (int taskID, int userID, GradeMethod gradeMethod) {
        try {
            return Globals.getDatabase ().getPlagItem (taskID, userID, gradeMethod);
        } catch (DatabaseException ex) {
            return null;
        }
    }
}
