package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.database.structure.TaskItem;
import java.util.List;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IPlagResult {

    List<IPlagPair> getPlags ();



    public QueueItem getQueueItem ();



    boolean isSimpleCheck ();
}
