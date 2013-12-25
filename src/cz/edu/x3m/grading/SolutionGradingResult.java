package cz.edu.x3m.grading;

import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.time.TimeGradeResult;

/**
 *
 * @author Jan Hybs
 */
public class SolutionGradingResult extends GradingResult implements ISolutionGradingResult {

    private OutputGradeResult outputGradeResult;
    private TimeGradeResult timeGradeResult;
    private MemoryGradeResult memoryGradeResult;



    public SolutionGradingResult (double solutionResult) {
        super (solutionResult);
    }



    @Override
    public OutputGradeResult getOutputGradeResult () {
        return outputGradeResult;
    }



    @Override
    public void setOutputGradeResult (OutputGradeResult outputGradeResult) {
        this.outputGradeResult = outputGradeResult;
    }



    @Override
    public TimeGradeResult getTimeGradeResult () {
        return timeGradeResult;
    }



    @Override
    public void setTimeGradeResult (TimeGradeResult timeGradeResult) {
        this.timeGradeResult = timeGradeResult;
    }



    @Override
    public MemoryGradeResult getMemoryGradeResult () {
        return memoryGradeResult;
    }



    @Override
    public void setMemoryGradeResult (MemoryGradeResult memoryGradeResult) {
        this.memoryGradeResult = memoryGradeResult;
    }
}
