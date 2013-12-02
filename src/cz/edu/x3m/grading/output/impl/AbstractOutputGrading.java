package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.AbstractGrading;
import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.GradingSetting;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.grading.exception.GradingRuntimeException;
import cz.edu.x3m.grading.output.OutputGradeSetting;
import java.io.File;
import java.io.IOException;

/**
 * @author Jan Hybs
 */
abstract public class AbstractOutputGrading extends AbstractGrading {


    @Override
    public GradingResult grade () throws GradingException {
        try {
            OutputGradeSetting settings = (OutputGradeSetting) this.settings;
            return compare (settings.getOriginalFile (), settings.getComparedFile ());
        } catch (IOException ex) {
            throw new GradingException (ex);
        }
    }


    abstract protected GradingResult compare (File originalFile, File comparedFile)throws IOException;


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
