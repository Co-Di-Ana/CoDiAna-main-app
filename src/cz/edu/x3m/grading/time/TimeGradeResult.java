package cz.edu.x3m.grading.time;

import cz.edu.x3m.grading.GradingResult;

/**
 *
 * @author Jan Hybs
 */
public class TimeGradeResult extends GradingResult {

    private final int runTime;



    public TimeGradeResult (double timeResult, int runTime) {
        super (timeResult);
        this.runTime = runTime;
    }



    public int getRunTime () {
        return runTime;
    }
}
