package cz.edu.x3m.database.data;

import cz.edu.x3m.database.exception.InvalidArgument;


/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum FinalGradeMode {

    /**
     * final result will not be calculated
     */
    TEST (1),
    /**
     * final result will be calculated value between 0-1 if output is correct
     * depending on average of measured values (such as time, memory) or zero if
     * output is not entirely correct
     */
    PRECISE (2),
    /**
     * final result will be calculated as average of ALL measured values (time,
     * output and memory)
     */
    MEASURE (3);
    // ---------------------------------------------------------------------------------------------
    private final int value;


    private FinalGradeMode (int value) {
        this.value = value;
    }


    public static FinalGradeMode create (int value) throws InvalidArgument {
        for (FinalGradeMode finalGradeMode : values ())
            if (finalGradeMode.value == value)
                return finalGradeMode;
        throw new InvalidArgument ("Invalid value");
    }


    public int value () {
        return value;
    }
}
