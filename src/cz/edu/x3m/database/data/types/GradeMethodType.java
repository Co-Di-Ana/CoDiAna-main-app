package cz.edu.x3m.database.data.types;


import cz.edu.x3m.database.exception.InvalidArgument;



/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum GradeMethodType {

    STRICT (1), TOLERANT (2), VAGUE (3),
    THRESHOLD (1);
    private final int value;


    private GradeMethodType (int value) {
        this.value = value;
    }


    public int value () {
        return value;
    }


    public static GradeMethodType getByOutputValue (int value) throws InvalidArgument {
        switch (value) {
            case 1:
                return STRICT;
            case 2:
                return TOLERANT;
            case 3:
                return VAGUE;
        }
        throw new InvalidArgument ("Invalid value");
    }


    public static GradeMethodType getByTimeValue (int value) throws InvalidArgument {
        switch (value) {
            case 1:
                return THRESHOLD;
        }
        throw new InvalidArgument ("Invalid value");
    }


    public static GradeMethodType getByMemoryValue (int value) throws InvalidArgument {
        switch (value) {
            case 1:
                return THRESHOLD;
        }
        throw new InvalidArgument ("Invalid value");
    }
}