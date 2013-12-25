package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.utils.Strings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Jan Hybs
 */
public class TolerantOutputGrading extends AbstractOutputGrading {

    @Override
    protected GradingResult compare (File originalFile, File comparedFile) throws IOException {
        BufferedReader origReader = new BufferedReader (new FileReader (originalFile));
        BufferedReader userReader = new BufferedReader (new FileReader (comparedFile));

        String origLine = "";
        String userLine = "";
        boolean origEnd = false;
        boolean userEnd = false;

        int correctLines = 0;
        int incorrectLines = 0;

        while (!origEnd && !userEnd) {

            // read original lines (skip white lines)
            if (!origEnd) {
                do {
                    origLine = origReader.readLine ();
                    if (origLine == null) {
                        origEnd = true;
                        break;
                    } else {
                        if (Strings.isOnlyWhitespace (origLine))
                            continue;
                        break;
                    }
                } while (true);
            }

            // read original lines (skip white lines)
            if (!userEnd) {
                do {
                    userLine = userReader.readLine ();
                    if (userLine == null) {
                        userEnd = true;
                        break;
                    } else {
                        if (Strings.isOnlyWhitespace (userLine))
                            continue;
                        break;
                    }
                } while (true);
            }

            //# both of them finished?
            if (origEnd && userEnd)
                break;

            // get rid of null values
            origLine = Strings.empty (origLine);
            userLine = Strings.empty (userLine);

            if (origLine.equals (userLine))
                correctLines++;
            else
                incorrectLines++;
        }

        double result = correctLines / ((double) (correctLines + incorrectLines));
        return this.result = new OutputGradeResult (result, correctLines, incorrectLines);
    }
}
