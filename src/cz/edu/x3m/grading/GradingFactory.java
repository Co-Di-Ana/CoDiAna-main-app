package cz.edu.x3m.grading;

import cz.edu.x3m.database.data.types.GradeMethodType;
import cz.edu.x3m.grading.memory.impl.MemoryGrading;
import cz.edu.x3m.grading.output.impl.StrictOutputGrading;
import cz.edu.x3m.grading.output.impl.TolerantOutputGrading;
import cz.edu.x3m.grading.output.impl.VagueOutputGrading;
import cz.edu.x3m.grading.time.impl.ThresholdTimeGrading;

/**
 *
 * @author Jan Hybs
 */
public class GradingFactory {

    public static IGrading getOutputGradingInstance (GradeMethodType type) {
        switch (type) {
            case TOLERANT:
                return new TolerantOutputGrading ();
            case STRICT:
                return new StrictOutputGrading ();
            case VAGUE:
                return new VagueOutputGrading ();
            default:
                return null;
        }
    }



    public static IGrading getTimeGradingInstance (GradeMethodType type) {
        switch (type) {
            case THRESHOLD:
                return new ThresholdTimeGrading ();
            default:
                return null;
        }
    }



    public static IGrading getMemoryGradingInstance (GradeMethodType type) {
        switch (type) {
            default:
                return new MemoryGrading ();
        }
    }
}
