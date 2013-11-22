package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.exception.GradingRuntimeException;
import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.time.TimeGradeResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public class SolutionGrading extends AbstractGrading {

    private final List<IGrading> gradings = new ArrayList<> ();



    public void addGrading (IGrading grading) {
        GradingType type = grading.getType ();

        for (int i = 0; i < gradings.size (); i++) {
            if (gradings.get (i).getType () == type)
                throw new GradingRuntimeException ("Grading type '%s' is already present", type);

        }

        gradings.add (grading);
    }



    @Override
    public GradingResult grade () throws GradingException {
        super.grade ();

        int totalGradings = 0;
        double totalResult = 0;

        IGrading grading;
        for (int i = 0; i < gradings.size (); i++) {
            grading = gradings.get (i);
            grading.grade ();

            totalResult += grading.getGradingResult ().getResult ();
            totalGradings++;
        }

        if (totalGradings == 0)
            throw new GradingException ("No gradings criteria found");



        SolutionGradingResult result = new SolutionGradingResult (totalResult / totalGradings);

        if ((grading = getGrading (GradingType.Output)) != null)
            result.setOutputGradeResult ((OutputGradeResult) grading.getGradingResult ());

        if ((grading = getGrading (GradingType.Time)) != null)
            result.setTimeGradeResult ((TimeGradeResult) grading.getGradingResult ());

        if ((grading = getGrading (GradingType.Memory)) != null)
            result.setMemoryGradeResult ((MemoryGradeResult) grading.getGradingResult ());


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
    public void setSettings (GradingSetting setting) {
        super.setSettings (setting);
        if (!(setting instanceof SolutionGradingSetting))
            throw new GradingRuntimeException ("Setting must be SolutionGradingSetting type");
    }



    @Override
    public GradingType getType () {
        return GradingType.Final;
    }
}
