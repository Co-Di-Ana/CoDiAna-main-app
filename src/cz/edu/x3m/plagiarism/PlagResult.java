package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.database.structure.TaskItem;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public class PlagResult implements IPlagResult {

    private final List<IPlagPair> plags;
    private final boolean isSimpleCheck;
    private final QueueItem queueItem;



    public PlagResult (QueueItem queueItem, List<IPlagPair> plags, boolean isSimpleCheck) {
        this.plags = plags;
        this.queueItem = queueItem;
        this.isSimpleCheck = isSimpleCheck;
    }



    @Override
    public List<IPlagPair> getPlags () {
        return plags;
    }



    @Override
    public QueueItem getQueueItem () {
        return queueItem;
    }



    @Override
    public boolean isSimpleCheck () {
        return isSimpleCheck;
    }
}
