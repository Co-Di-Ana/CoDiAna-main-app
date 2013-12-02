package cz.edu.x3m.grading;

import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.time.TimeGradeResult;

/**
 *
 * @author Jan Hybs
 */
public class SolutionGradingResult extends GradingResult {

    private OutputGradeResult outputGradeResult;
    private TimeGradeResult timeGradeResult;
    private MemoryGradeResult memoryGradeResult;



    public SolutionGradingResult (double solutionResult) {
        super (solutionResult);
    }



    /**
     * @return the outputGradeResult
     */
    public OutputGradeResult getOutputGradeResult () {
        return outputGradeResult;
    }



    /**
     * @param outputGradeResult the outputGradeResult to set
     */
    public void setOutputGradeResult (OutputGradeResult outputGradeResult) {
        this.outputGradeResult = outputGradeResult;
    }



    /**
     * @return the timeGradeResult
     */
    public TimeGradeResult getTimeGradeResult () {
        return timeGradeResult;
    }



    /**
     * @param timeGradeResult the timeGradeResult to set
     */
    public void setTimeGradeResult (TimeGradeResult timeGradeResult) {
        this.timeGradeResult = timeGradeResult;
    }



    /**
     * @return the memoryGradeResult
     */
    public MemoryGradeResult getMemoryGradeResult () {
        return memoryGradeResult;
    }



    /**
     * @param memoryGradeResult the memoryGradeResult to set
     */
    public void setMemoryGradeResult (MemoryGradeResult memoryGradeResult) {
        this.memoryGradeResult = memoryGradeResult;
    }
}
