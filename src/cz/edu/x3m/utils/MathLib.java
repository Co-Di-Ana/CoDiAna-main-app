package cz.edu.x3m.utils;

import cz.edu.x3m.grading.time.TimeGradeResult;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class MathLib {

    public static double getRelativeBisectedValue (double value, double min, double max) {
        double result = Double.NaN;

        // too bad
        if (value >= max)
            return 0.0;

        // perfect
        if (value < min)
            return 1.0;

        // align to 0.0 - 1.0
        result = (value - min);
        result /= (max - min);
        return 1.0 - result;
    }
}
