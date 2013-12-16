package cz.edu.x3m.database.data;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
abstract public class AbstractDetailItem {

    private final int userID;
    private final int taskID;



    public AbstractDetailItem (int taskID, int userID) {
        this.userID = userID;
        this.taskID = taskID;
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
}
