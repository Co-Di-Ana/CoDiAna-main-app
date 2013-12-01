package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingRuntimeException;
import cz.edu.x3m.grading.output.impl.StrictOutputGrading;
import cz.edu.x3m.grading.time.impl.ThresholdTimeGrading;

/**
 *
 * @author Jan Hybs
 */
public class GradingFactory {

    public static final String TYPE_OUTPUT_STRICT = "strict";
    public static final String TYPE_OUTPUT_NORMAL = "normal";
    public static final String TYPE_OUTPUT_VAGUE = "vague";
    public static final String TYPE_TIME_STRICT = "strict";
    public static final String TYPE_TIME_THRESHOLD = "threshold";
    public static final String TYPE_TIME_VAGUE = "vague";
    public static final String TYPE_MEMORY_STRICT = "strict";
    public static final String TYPE_MEMORY_NORMAL = "threshold";
    public static final String TYPE_MEMORY_VAGUE = "vague";



    public static IGrading getInstance (GradingType type, String name) {
        switch (type) {
            case OUTPUT:
                return getOutputInstance (name);

            case TIME:
                return getTimeInstance (name);

            case MEMORY:
                return getMemoryInstance (name);
        }

        throw new GradingRuntimeException ("Unspecified grading type '%s'", name);
    }



    private static IGrading getOutputInstance (String name) {
        if (name.equalsIgnoreCase (TYPE_OUTPUT_STRICT))
            return new StrictOutputGrading ();

        throw new GradingRuntimeException ("Unknown OUTPUT grading '%s'", name);
    }



    private static IGrading getTimeInstance (String name) {
        if (name.equalsIgnoreCase (TYPE_TIME_THRESHOLD))
            return new ThresholdTimeGrading ();

        throw new GradingRuntimeException ("Unknown TIME grading '%s'", name);
    }



    private static IGrading getMemoryInstance (String name) {

        throw new GradingRuntimeException ("Unknown MEMORY grading " + name);
    }
}
