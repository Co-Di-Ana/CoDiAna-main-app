package cz.edu.x3m.database.data;

import cz.edu.x3m.database.exception.InvalidArgument;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum GradeMethod {

    /**
     * will be chosen first sent solution 
     */
    FIRST (1),
    /**
     * will be chosen last sent solution
     */
    LAST (2),
    /**
     * will be chosen the best solution sent
     */
    BEST (3);
    // ---------------------------------------------------------------------------------------------
    private final int value;



    private GradeMethod (int value) {
        this.value = value;
    }



    public static GradeMethod create (int value) throws InvalidArgument {
        for (GradeMethod gradeMethod : values ())
            if (gradeMethod.value == value)
                return gradeMethod;
        throw new InvalidArgument ("Invalid value");
    }
    

    public int value () {
        return value;
    }
}
