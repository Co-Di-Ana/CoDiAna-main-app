package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.utils.InputTokenizer;
import cz.edu.x3m.utils.Strings;
import java.io.File;
import java.io.IOException;

/**
 * @author Jan Hybs
 */
public class VagueOutputGrading extends AbstractOutputGrading {

    @Override
    protected GradingResult compare (File originalFile, File comparedFile) throws IOException {

        InputTokenizer origTokenizer = new InputTokenizer (originalFile);
        InputTokenizer userTokenizer = new InputTokenizer (comparedFile);

        String origToken = "";
        String userToken = "";
        boolean origEnd = false;
        boolean userEnd = false;

        int correctLines = 0;
        int incorrectLines = 0;

        while (!origEnd && !userEnd) {

            // read original lines (skip white lines)
            if (!origEnd) {
                if ((origToken = origTokenizer.nextToken ()) == null)
                    origEnd = true;
            }

            // read original lines (skip white lines)
            if (!userEnd) {
                if ((userToken = userTokenizer.nextToken ()) == null)
                    userEnd = true;
            }

            //# both of them finished?
            if (origEnd && userEnd)
                break;

            // get rid of null values
            origToken = Strings.empty (origToken);
            userToken = Strings.empty (userToken);

            if (origToken.equals (userToken))
                correctLines++;
            else
                incorrectLines++;
        }

        double result = correctLines / ((double) (correctLines + incorrectLines));
        return this.result = new OutputGradeResult (result, correctLines, incorrectLines);
    }
}
