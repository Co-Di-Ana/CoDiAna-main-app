package cz.edu.x3m.database.data;

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
}
