package cz.edu.x3m.database.structure;

import cz.edu.x3m.database.exception.InvalidArgument;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PlagItem extends AbstractDataObject implements IPlagObject {

    private List<AttemptItem> items = new ArrayList<> ();
    private final int taskID;
    private final int userID;



    public PlagItem (int taskID, int userID, ResultSet row) throws SQLException, InvalidArgument {
        super (row);
        this.taskID = taskID;
        this.userID = userID;

        while (row.next ())
            items.add (new AttemptItem (row));
    }



    public int getTaskID () {
        return taskID;
    }



    public int getUserID () {
        return userID;
    }



    @Override
    public List<AttemptItem> getItems () {
        return items;
    }
}
