package cz.edu.x3m.database.data;

import java.sql.ResultSet;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PlagiarismCheckItem extends AbstractDetailItem {

    public PlagiarismCheckItem (int taskID, int relatedID, ResultSet row) {
        super (taskID, relatedID);
    }
}
