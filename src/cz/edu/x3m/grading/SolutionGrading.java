package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.time.TimeGradeResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public class SolutionGrading extends AbstractGrading implements ISolutionGrading {

    private final List<IGrading> gradings = new ArrayList<> ();



    @Override
    public GradingResult grade () throws GradingException {
        super.grade ();

        IGrading grading;
        double finalResult = -1;
        double totalResult = 0;
        int i;
        for (i = 0; i < gradings.size (); i++)
            gradings.get (i).grade ();


        switch (queueItem.getTaskItem ().getFinalGradeMode ()) {
            case TEST:
            default:
                finalResult = -1;
                break;
                
            case MEASURE:
                for (i = 0; i < gradings.size (); i++)
                    totalResult += gradings.get (i).getResult ().getResult ();
                finalResult = totalResult / gradings.size ();
                break;
                
            case PRECISE:
                int totalGradings = 0;
                for (i = 0; i < gradings.size (); i++) {
                    grading = gradings.get (i);

                    if (grading.getType () == GradingType.OUTPUT || grading.getType () == GradingType.FINAL)
                        continue;

                    totalResult += gradings.get (i).getResult ().getResult ();
                    totalGradings++;
                }
                finalResult = totalResult / totalGradings;
                break;
        }

        SolutionGradingResult result = new SolutionGradingResult (finalResult);

        if ((grading = getGrading (GradingType.OUTPUT)) != null)
            result.setOutputGradeResult ((OutputGradeResult) grading.getResult ());

        if ((grading = getGrading (GradingType.TIME)) != null)
            result.setTimeGradeResult ((TimeGradeResult) grading.getResult ());

        if ((grading = getGrading (GradingType.MEMORY)) != null)
            result.setMemoryGradeResult ((MemoryGradeResult) grading.getResult ());


        return result;
    }



    private IGrading getGrading (GradingType type) {
        for (int i = 0; i < gradings.size (); i++) {
            if (gradings.get (i).getType () == type)
                return gradings.get (i);
        }

        return null;
    }



    @Override
    public GradingType getType () {
        return GradingType.FINAL;
    }



    @Override
    public void addMonitors () {
        addMonitor (GradingFactory.getOutputGradingInstance (
                queueItem.getTaskItem ().getOutputMethod ()));

        addMonitor (GradingFactory.getTimeGradingInstance (
                queueItem.getTaskItem ().getTimeMethod ()));

        addMonitor (GradingFactory.getMemoryGradingInstance (
                queueItem.getTaskItem ().getMemoryMethod ()));
    }



    private boolean addMonitor (IGrading grading) {
        if (grading == null)
            return false;
        grading.setQueueItem (queueItem);
        gradings.add (grading);
        return true;
    }
}
