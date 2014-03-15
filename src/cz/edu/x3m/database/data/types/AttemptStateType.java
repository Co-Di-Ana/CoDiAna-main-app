package cz.edu.x3m.database.data.types;


import cz.edu.x3m.database.exception.InvalidArgument;



/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public enum AttemptStateType {

    // ---------------------------------------------------------------------------------------------
    // ------- SPECIAL STATES ----------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Other non specific error
     */
    OTHER_ERROR (0),
    /**
     * Processing has been aborted
     */
    PROCESS_ABORTED (99),
    /**
     * Waiting in queue for processing
     */
    WAITING_TO_PROCESS (1),
    // ---------------------------------------------------------------------------------------------
    // ------- CODE SPECIFIC STATES ----------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Source code containg dangerous elements (packages, commands, etc.)
     */
    CODE_DANGEROUS (2),
    /**
     * Source code in either in non-readable state, or has broken rules for this task
     */
    CODE_INVALID (3),
    /**
     * Source code is valid and can be processed
     */
    CODE_VALID (4),
    // ---------------------------------------------------------------------------------------------
    // ------- COMPILATION STATES ------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Error during compilation
     */
    COMPILATION_ERROR (5),
    /**
     * Compilation was not completed in time, usually broken code
     */
    COMPILATION_TIMEOUT (6),
    /**
     * Compilation was successful
     */
    COMPILATION_OK (7),
    // ---------------------------------------------------------------------------------------------
    // ------- RUN STATES --------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Error during execution
     */
    EXECUTION_ERROR (8),
    /**
     * Execution was not completed in time and was be terminated
     */
    EXECUTION_TIMEOUT (9),
    /**
     * Execution was successful
     */
    EXECUTION_OK (10),
    // ---------------------------------------------------------------------------------------------
    // ------- MEASURE STATES ----------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Task required output to be correct and it was not correct
     */
    OUTPUT_ERROR (11),
    /**
     * Task required execution time to be lower than some threshold and it took longer to execute
     */
    TIME_ERROR (12),
    /**
     * Task required execution memory peak to be lower than some threshold and it memory peak was
     * overstepped
     */
    MEMORY_ERROR (13),
    /**
     * Measurement was successful
     */
    MEASUREMENT_OK (14);
    //
    // ---------------------------------------------------------------------------------------------
    //
    private final int value;



    private AttemptStateType (int value) {
        this.value = value;
    }



    /**
     * Method creates AttemptStateType from given unique id
     *
     * @param value unique code
     * @return AttemptStateType or throws Exception if unique id is not found
     * @throws InvalidArgument if unique id is not found
     */
    public static AttemptStateType create (int value) throws InvalidArgument {
        for (AttemptStateType taskState : values ())
            if (taskState.value == value)
                return taskState;
        throw new InvalidArgument ("Invalid state type");
    }



    public int value () {
        return value;
    }
}