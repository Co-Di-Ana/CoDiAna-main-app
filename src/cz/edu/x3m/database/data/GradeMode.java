package cz.edu.x3m.database.data;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum GradeMode{

    /**
     * final result will not be calculated
     */
    TEST,
    /**
     * final result will be calculated value between 0-1 if output si correct depending on average
     * of measered values (such as time, memory) or zero if output is not entirely correct
     */
    PRECISE,
    /**
     * final result will be calculated as average of ALL measured values (time, output and memory)
     */
    MEASURE;
    // ---------------------------------------------------------------------------------------------
    private int value;



    public static GradeMode create (int value) throws InvalidArgument {
        for (GradeMode gradeMode : values ())
            if (gradeMode.value == value)
                return gradeMode;
        throw new InvalidArgument ("Invalid value");
    }
}
