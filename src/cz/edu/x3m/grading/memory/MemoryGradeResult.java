package cz.edu.x3m.grading.memory;

import cz.edu.x3m.grading.GradingResult;

/**
 *
 * @author Jan Hybs
 */
public class MemoryGradeResult extends GradingResult {

    private final int memoryPeak;



    public MemoryGradeResult (double timeResult, int memoryPeak) {
        super (timeResult);
        this.memoryPeak = memoryPeak;
    }



    public int getMemoryPeak () {
        return memoryPeak;
    }
}
