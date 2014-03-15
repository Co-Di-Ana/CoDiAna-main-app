package cz.edu.x3m.grading.time.impl;

import cz.edu.x3m.grading.AbstractGrading;
import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.grading.time.TimeGradeResult;
import cz.edu.x3m.utils.MathLib;

/**
 * @author Jan Hybs
 */
public class ThresholdTimeGrading extends AbstractGrading {

    @Override
    public GradingResult grade () throws GradingException {
        int runTime = queueItem.getAttemptItem ().getExecutionResult ().getRunTime ();
        
        // is not set?
        if (!queueItem.getTaskItem ().isLimitTimeSet ())
            return this.result = new TimeGradeResult (GradingResult.MAX, runTime);
        
        
        double timeResult = MathLib.getRelativeBisectedValue (
                runTime,
                queueItem.getTaskItem ().getLimitTimeFalling (),
                queueItem.getTaskItem ().getLimitTimeNothing ());

        return this.result = new TimeGradeResult (timeResult, runTime);


    }



    @Override
    public GradingType getType () {
        return GradingType.TIME;
    }
}
