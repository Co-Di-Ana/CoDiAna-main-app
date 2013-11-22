package cz.edu.x3m.database.data;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
abstract public class AbstractDetailItem {

    protected final int relatedID;
    protected final int taskID;



    public AbstractDetailItem (int taskID, int relatedID) {
        this.relatedID = relatedID;
        this.taskID = taskID;
    }



    public int getRelatedID () {
        return relatedID;
    }



    public int getTaskID () {
        return taskID;
    }
}
