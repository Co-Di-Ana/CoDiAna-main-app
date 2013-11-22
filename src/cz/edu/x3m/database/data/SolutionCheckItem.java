package cz.edu.x3m.database.data;

import java.sql.ResultSet;

/**
 *
 * @author Jan Hybs
 */
public class SolutionCheckItem extends AbstractDetailItem {

    private String language;
    private String name;
    private boolean isComplex;



    public SolutionCheckItem (int taskID, int relatedID, ResultSet row) {
        super (taskID, relatedID);
    }
}
