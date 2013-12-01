package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.AbstractGrading;
import cz.edu.x3m.grading.time.TimeGradeSetting;
import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.GradingSetting;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.grading.exception.GradingRuntimeException;
import cz.edu.x3m.grading.output.OutputGradeSetting;

/**
 * @author Jan Hybs
 */
public class StrictOutputGrading extends AbstractGrading {

    @Override
    public GradingResult grade () throws GradingException {
        return null;
    }



    @Override
    public void setSettings (GradingSetting settings) {
        super.setSettings (settings);
        if (!(settings instanceof OutputGradeSetting))
            throw new GradingRuntimeException ("Settings must by type OutputGradeSetting");

    }



    @Override
    public GradingType getType () {
        return GradingType.OUTPUT;
    }
}
