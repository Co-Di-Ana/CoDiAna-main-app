/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.edu.x3m.database.data.types;


import cz.edu.x3m.database.exception.InvalidArgument;



public enum QueueItemType {

    TYPE_SOLUTION_CHECK (1),
    TYPE_PLAGIARISM_CHECK (3),
    TYPE_MEASURE_VALUES (2),
    TYPE_UNKNOWN (0);
    private final int value;


    private QueueItemType (int value) {
        this.value = value;
    }


    /**
     * Method creates QueueType from given unique id
     *
     * @param value unique code
     * @return QueueType or throws Exception if unique id is not found
     * @throws Exception if unique id is not found
     */
    public static QueueItemType create (int value) throws InvalidArgument {
        for (QueueItemType taskState : values ())
            if (taskState.value == value)
                return taskState;
        throw new InvalidArgument ("Invalid state type");
    }


    public int value () {
        return value;
    }
}