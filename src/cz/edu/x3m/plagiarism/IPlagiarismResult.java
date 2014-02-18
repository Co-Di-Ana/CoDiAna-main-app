package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.data.TaskItem;
import java.util.List;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IPlagiarismResult {

    List<IPlagiasrismPair> getPlags ();



    public TaskItem getTaskItem ();
}
