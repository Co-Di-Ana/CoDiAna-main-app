package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.data.TaskItem;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public class PlagiarismResult implements IPlagiarismResult {

    private List<IPlagiasrismPair> plags;
    private TaskItem taskItem;



    public PlagiarismResult (TaskItem taskItem, List<IPlagiasrismPair> plags) {
        this.plags = plags;
        this.taskItem = taskItem;
    }



    @Override
    public List<IPlagiasrismPair> getPlags () {
        return plags;
    }


    @Override
    public TaskItem getTaskItem () {
        return taskItem;
    }
    
    
}
