package cz.edu.x3m.database.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author Jan Hybs
 */
public class TaskItem {

    private final int id;
    private final String name;
    private final String mainFileName;
    private final int gradeMethod;
    private final int difficulty;
    private final String[] languages;
    private final Timestamp timeOpen;
    private final Timestamp timeClose;
    private final int limitMemoryFalling;
    private final int limitMemoryNothing;
    private final int limitTimeFalling;
    private final int limitTimeNothing;



    public TaskItem (ResultSet row) throws SQLException {

        name = row.getString ("name");
        mainFileName = row.getString ("taskmainfilename");
        languages = getArray (row.getString ("tasklanguages"));

        timeOpen = getTimestamp (row.getLong ("tasktimeopen"));
        timeClose = getTimestamp (row.getLong ("tasktimeclose"));

        id = row.getInt ("taskid");
        limitMemoryFalling = row.getInt ("tasklimitmemoryfalling");
        limitMemoryNothing = row.getInt ("tasklimitmemorynothing");
        limitTimeFalling = row.getInt ("tasklimittimefalling");
        limitTimeNothing = row.getInt ("tasklimittimenothing");
        gradeMethod = row.getInt ("taskgradeMethod");
        difficulty = row.getInt ("taskdifficulty");
    }



    private Timestamp getTimestamp (long timestamp) throws SQLException {
        return timestamp == 0 ? null : new Timestamp (timestamp);
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
     * @return the gradeMethod
     */
    public int getGradeMethod () {
        return gradeMethod;
    }



    /**
     * @return the difficulty
     */
    public int getDifficulty () {
        return difficulty;
    }



    /**
     * @return the languages
     */
    public String[] getLanguages () {
        return languages;
    }



    /**
     * @return the timeOpen
     */
    public Timestamp getTimeOpen () {
        return timeOpen;
    }



    /**
     * @return the timeClose
     */
    public Timestamp getTimeClose () {
        return timeClose;
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
}
