package cz.edu.x3m.grading;

import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.grading.exception.GradingException;

/**
 *
 * @author Jan Hybs
 */
public interface IGrading {

    /**
     * Method sets queue item value
     *
     * @param item
     */
    void setQueueItem (QueueItem item);



    /**
     * Method return current value of queue item
     *
     * @return queue item or null
     */
    QueueItem getQueueItem ();



    /**
     * Method runs algorithm for detecting desired value
     *
     * @return object containing result of the algorithm
     * @throws GradingException when error occurs
     */
    GradingResult grade () throws GradingException;



    /**
     * Method returns type of this object
     *
     * @return type of this object
     */
    GradingType getType ();



    /**
     * Method only retuns object containing result of the algorithm
     *
     * @return object containing result of the algorithm
     * @see IGrading#grade() 
     */
    GradingResult getResult ();
}
