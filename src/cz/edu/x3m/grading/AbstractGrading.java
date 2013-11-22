package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.exception.GradingRuntimeException;

/**
 *
 * @author Jan Hybs
 */
abstract public class AbstractGrading implements IGrading {

    protected GradingSetting settings;
    protected GradingResult result;



    @Override
    public void setSettings (GradingSetting settings) {
        if (settings == null)
            throw new GradingRuntimeException ("Settings cannot be null");
        this.settings = settings;
    }



    @Override
    public GradingSetting getSettings () {
        return settings;
    }



    @Override
    public GradingResult grade () throws GradingException {
        if (settings == null)
            throw new GradingException ("Settings cannot be null");
        return null;
    }



    @Override
    public GradingResult getGradingResult () {
        return result;
    }
}
