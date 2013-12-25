package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.AbstractGrading;
import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.utils.PathResolver;
import java.io.File;
import java.io.IOException;

/**
 * @author Jan Hybs
 */
abstract public class AbstractOutputGrading extends AbstractGrading {

    @Override
    public GradingResult grade () throws GradingException {
        try {
            File originalFile = new File (PathResolver.get (PathResolver.PathType.TASK_OUTPUT, queueItem));
            File comparedFile = new File (PathResolver.get (PathResolver.PathType.EXECUTION_OUTPUT, queueItem));
            return compare (originalFile, comparedFile);
        } catch (IOException ex) {
            throw new GradingException (ex);
        }
    }



    abstract protected GradingResult compare (File originalFile, File comparedFile) throws IOException;



    @Override
    public GradingType getType () {
        return GradingType.OUTPUT;
    }
}
