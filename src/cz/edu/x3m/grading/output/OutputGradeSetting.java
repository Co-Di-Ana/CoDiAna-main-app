package cz.edu.x3m.grading.output;

import cz.edu.x3m.grading.GradingSetting;
import java.io.File;

/**
 *
 * @author Jan Hybs
 */
public class OutputGradeSetting extends GradingSetting {

    private final File originalFile;
    private final File comparedFile;



    public OutputGradeSetting (boolean isManaging, File originalFile, File comparedFile) {
        super (isManaging);
        this.originalFile = originalFile;
        this.comparedFile = comparedFile;
    }



    public File getOriginalFile () {
        return originalFile;
    }



    public File getComparedFile () {
        return comparedFile;
    }
}
