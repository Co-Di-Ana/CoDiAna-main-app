package cz.edu.x3m.database.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PlagiarismSolutionList extends AbstractDetailItem implements IPlagiarismSolutionList {

    private List<ISolution> items = new ArrayList<> ();



    public PlagiarismSolutionList (int taskID, int relatedID, ResultSet row) throws SQLException {
        super (taskID, relatedID);

        while (row.next ())
            items.add (new Solution (row));
    }



    @Override
    public List<ISolution> getItems () {
        return items;
    }
}
