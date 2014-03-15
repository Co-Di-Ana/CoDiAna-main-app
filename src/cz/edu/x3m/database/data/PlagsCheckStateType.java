package cz.edu.x3m.database.data;

import cz.edu.x3m.database.exception.InvalidArgument;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum PlagsCheckStateType {

    /**
     * Waiting in queue for processing
     */
    NOT_EXECUTED (1),
    /**
     * Waiting in queue for processing
     */
    WAITING_TO_PROCESS (2),
    /**
     * Some duplicates were found
     */
    PLAGS_FOUND (3),
    /**
     * Some duplicates were found
     */
    NO_PLAGS_FOUND (4),
    /**
     * Processing has been aborted
     */
    PROCESS_ABORTED (99);
    //------------------------
    private final int value;



    private PlagsCheckStateType (int value) {
        this.value = value;
    }



    /**
     * Method creates PlagsCheckStateType from given unique id
     *
     * @param value unique code
     * @return AttemptStateType or throws Exception if unique id is not found
     * @throws InvalidArgument if unique id is not found
     */
    public static PlagsCheckStateType create (int value) throws InvalidArgument {
        for (PlagsCheckStateType taskState : values ())
            if (taskState.value == value)
                return taskState;
        throw new InvalidArgument ("Invalid state type");
    }



    public int value () {
        return value;
    }
}
