package cz.edu.x3m.database.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jan Hybs
 */
public class TaskItem {

    private final int id;
    private final String name;
    private final String mainFileName;
    private final OutputMethodType outputMethod;
    private final String[] languages;
    private final int limitMemoryFalling;
    private final int limitMemoryNothing;
    private final int limitTimeFalling;
    private final int limitTimeNothing;



    public TaskItem (ResultSet row) throws SQLException {

        // basic informations
        id = row.getInt ("taskid");
        name = row.getString ("taskname");
        mainFileName = row.getString ("taskmainfilename");
        int type = row.getInt ("taskoutputmethod");
        outputMethod = type == 0 ? OutputMethodType.STRICT
                       : type == 1 ? OutputMethodType.TOLERANT
                         : type == 2 ? OutputMethodType.VAGUE
                           : OutputMethodType.UNKNOWN;
        languages = getArray (row.getString ("tasklanguages"));

        // limits
        limitTimeFalling = row.getInt ("tasklimittimefalling");
        limitTimeNothing = row.getInt ("tasklimittimenothing");
        limitMemoryFalling = row.getInt ("tasklimitmemoryfalling");
        limitMemoryNothing = row.getInt ("tasklimitmemorynothing");
    }



    private String[] getArray (String data) {
        return data == null || data.isEmpty () ? new String[]{} : data.split ("\\s*,\\s*");
    }



    /**
     * @return the id
     */
    public int getID () {
        return id;
    }



    /**
     * @return the name
     */
    public String getName () {
        return name;
    }



    /**
     * @return the mainFileName
     */
    public String getMainFileName () {
        return mainFileName;
    }



    /**
     * @return the languages
     */
    public String[] getLanguages () {
        return languages;
    }



    /**
     * @return the time threshold in ms where overstepping couses point loss 100 - 0 points awarded
     */
    public int getLimitMemoryFalling () {
        return limitMemoryFalling;
    }



    /**
     * @return the time threshold in ms after which is classification zero zero points awarded
     */
    public int getLimitMemoryNothing () {
        return limitMemoryNothing;
    }



    /**
     * @return the value threshold in kB where overstepping couses point loss 100 - 0 points awarded
     */
    public int getLimitTimeFalling () {
        return limitTimeFalling;
    }



    /**
     * @return the lvalue threshold in kB after which is classification zero zero points awarded
     */
    public int getLimitTimeNothing () {
        return limitTimeNothing;
    }



    /**
     * @return the outputMethod
     */
    public OutputMethodType getOutputMethod () {
        return outputMethod;
    }

    public static enum OutputMethodType {

        STRICT, TOLERANT, VAGUE, UNKNOWN
    }
}
