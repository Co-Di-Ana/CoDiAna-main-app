package cz.edu.x3m.grading.output;

import cz.edu.x3m.grading.GradingResult;

/**
 *
 * @author Jan Hybs
 */
public class OutputGradeResult extends GradingResult {

    private final int correctLines;



    public OutputGradeResult (double outputResult, int correctLines) {
        super (outputResult);
        this.correctLines = correctLines;
    }



    public int getCorrectLines () {
        return correctLines;
    }
}
