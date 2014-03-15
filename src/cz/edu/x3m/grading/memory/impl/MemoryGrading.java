package cz.edu.x3m.grading.memory.impl;

import cz.edu.x3m.grading.AbstractGrading;
import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.utils.MathLib;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class MemoryGrading extends AbstractGrading {

    @Override
    public GradingResult grade () throws GradingException {
        int memoryAvg = queueItem.getAttemptItem ().getExecutionResult ().getMemoryAvg ();

        // is not set?
        if (!queueItem.getTaskItem ().isLimitMemorySet ())
            return this.result = new MemoryGradeResult (GradingResult.MAX, memoryAvg);

        double timeResult = MathLib.getRelativeBisectedValue (
                memoryAvg,
                queueItem.getTaskItem ().getLimitMemoryFalling (),
                queueItem.getTaskItem ().getLimitMemoryNothing ());

        return this.result = new MemoryGradeResult (timeResult, memoryAvg);
    }



    @Override
    public GradingType getType () {
        return GradingType.MEMORY;
    }
}
