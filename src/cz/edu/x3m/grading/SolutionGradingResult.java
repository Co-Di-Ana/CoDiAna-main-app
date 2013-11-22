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



    public void setOutputGradeResult (OutputGradeResult outputGradeResult) {
        this.outputGradeResult = outputGradeResult;
    }



    public void setTimeGradeResult (TimeGradeResult timeGradeResult) {
        this.timeGradeResult = timeGradeResult;
    }



    public void setMemoryGradeResult (MemoryGradeResult memoryGradeResult) {
        this.memoryGradeResult = memoryGradeResult;
    }
}
