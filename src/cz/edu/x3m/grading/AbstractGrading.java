package cz.edu.x3m.grading;

import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.grading.exception.GradingException;

/**
 *
 * @author Jan Hybs
 */
abstract public class AbstractGrading implements IGrading {

    protected QueueItem queueItem;
    protected GradingResult result;



    @Override
    public void setQueueItem (QueueItem item) {
        this.queueItem = item;
    }



    @Override
    public QueueItem getQueueItem () {
        return queueItem;
    }



    @Override
    public GradingResult grade () throws GradingException {
        if (queueItem == null)
            throw new GradingException ("QueueItem cannot be null");
        return null;
    }



    @Override
    public GradingResult getResult () {
        return result;
    }
}
