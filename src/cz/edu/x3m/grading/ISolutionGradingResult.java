package cz.edu.x3m.grading;

import cz.edu.x3m.grading.memory.MemoryGradeResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.time.TimeGradeResult;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface ISolutionGradingResult extends IGradingResult {

    /**
     * @return the outputGradeResult
     */
    public OutputGradeResult getOutputGradeResult ();



    /**
     * @param outputGradeResult the outputGradeResult to set
     */
    public void setOutputGradeResult (OutputGradeResult outputGradeResult);



    /**
     * @return the timeGradeResult
     */
    public TimeGradeResult getTimeGradeResult ();



    /**
     * @param timeGradeResult the timeGradeResult to set
     */
    public void setTimeGradeResult (TimeGradeResult timeGradeResult);



    /**
     * @return the memoryGradeResult
     */
    public MemoryGradeResult getMemoryGradeResult ();



    /**
     * @param memoryGradeResult the memoryGradeResult to set
     */
    public void setMemoryGradeResult (MemoryGradeResult memoryGradeResult);
}
