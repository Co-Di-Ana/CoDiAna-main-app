package cz.edu.x3m.grading.output;

import cz.edu.x3m.grading.GradingResult;

/**
 *
 * @author Jan Hybs
 */
public class OutputGradeResult extends GradingResult {

    private final int correctLines;
    private final int incorrectLines;



    public OutputGradeResult (double outputResult, int correctLines, int incorrectLines) {
        super (outputResult);
        this.correctLines = correctLines;
        this.incorrectLines = incorrectLines;
    }



    /**
     * @return the correctLines
     */
    public int getCorrectLines () {
        return correctLines;
    }



    /**
     * @return the incorrectLines
     */
    public int getIncorrectLines () {
        return incorrectLines;
    }
}
