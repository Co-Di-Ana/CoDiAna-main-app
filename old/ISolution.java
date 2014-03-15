package cz.edu.x3m.database.data;

import cz.edu.x3m.database.structure.AttemptObject;
import cz.edu.x3m.database.structure.TaskItem;
import cz.edu.x3m.database.structure.UserItem;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface ISolution {

    /**
     * @return the user ID
     */
    public int getUserID ();



    /**
     * @return the task ID
     */
    public int getTaskID ();



    /**
     * @return the attempt ID if exists or 0 if not
     */
    public int getAttemptID ();



    /**
     * @return the language extension (java, py)
     */
    public String getLanguage ();



    /**
     * @return ordinal number of attempt
     */
    public int getOrdinal ();



    /**
     * @return loads object from DB or return loaded instance, if error null returned
     */
    public TaskItem getTaskObject ();



    /**
     * @return loads object from DB or return loaded instance, if error null returned
     */
    public UserItem getUserObject ();



    /**
     * @return loads object from DB or return loaded instance, if error null returned
     */
    public AttemptObject getAttemptObject ();
}
