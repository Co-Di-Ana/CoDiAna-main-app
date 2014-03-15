package cz.edu.x3m.database.structure;

import cz.edu.x3m.database.data.FinalGradeMode;
import cz.edu.x3m.database.data.GradeMethod;
import cz.edu.x3m.database.exception.InvalidArgument;
import cz.edu.x3m.database.data.types.GradeMethodType;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jan Hybs
 */
public class TaskItem extends AbstractDataObject {

    private final int id;
    private final String name;
    private final String mainFileName;
    private final GradeMethodType outputMethod;
    private final GradeMethodType timeMethod;
    private final GradeMethodType memoryMethod;
    private final String[] languages;
    private final int limitMemoryFalling;
    private final int limitMemoryNothing;
    private final int limitTimeFalling;
    private final int limitTimeNothing;
    private final FinalGradeMode finalGradeMode;
    private final GradeMethod gradeMethod;
    public static final int NOT_SUPPORTED_YET = -1;



    public TaskItem (ResultSet row) throws SQLException, InvalidArgument {
        super (row);

        // basic informations
        id = provider.getInt ("id");
        name = provider.getString ("name");
        mainFileName = provider.getString ("mainfilename");
        outputMethod = GradeMethodType.getByOutputValue (provider.getInt ("outputmethod"));
        timeMethod = GradeMethodType.THRESHOLD;
        memoryMethod = GradeMethodType.THRESHOLD;
        languages = getArray (provider.getString ("languages"));

        // limits
        limitTimeFalling = provider.getInt ("limittimefalling");
        limitTimeNothing = provider.getInt ("limittimenothing");
        limitMemoryFalling = provider.getInt ("limitmemoryfalling");
        limitMemoryNothing = provider.getInt ("limitmemorynothing");
        // methods
        gradeMethod = GradeMethod.create (provider.getInt ("grademethod"));
        finalGradeMode = FinalGradeMode.PRECISE;
    }



    private String[] getArray (String data) {
        return data == null || data.isEmpty () ? new String[]{} : data.split ("\\s*,\\s*");
    }



    private File getFile (String extension) {
        return new File (
                String.format ("./%s/%s.%s",
                               String.format ("task-%04d", id),
                               mainFileName,
                               extension));
    }



    public File getOutputFile () {
        return getFile ("out");
    }



    public File getErrorFile () {
        return getFile ("err");
    }



    /**
     * @return the task id
     */
    public int getID () {
        return id;
    }



    /**
     * @return the task name
     */
    public String getName () {
        return name;
    }



    /**
     * @return the task mainFileName, e.g. class name
     */
    public String getMainFileName () {
        return mainFileName;
    }



    /**
     * @return array of supported languages (extensions)
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
    public GradeMethodType getOutputMethod () {
        return outputMethod;
    }



    public GradeMethodType getTimeMethod () {
        return timeMethod;
    }



    public GradeMethodType getMemoryMethod () {
        return memoryMethod;
    }



    /**
     * @return the gradeMethod
     */
    public GradeMethod getGradeMethod () {
        return gradeMethod;
    }



    /**
     * @return the finalGradeMode
     */
    public FinalGradeMode getFinalGradeMode () {
        return finalGradeMode;
    }



    /**
     * @return wheter are time limits not null
     */
    public boolean isLimitTimeSet () {
        return limitTimeNothing != 0 && limitTimeFalling != 0;
    }



    /**
     * @return wheter are memory limits not null
     */
    public boolean isLimitMemorySet () {
        return limitMemoryNothing != 0 && limitMemoryFalling != 0;
    }
}
