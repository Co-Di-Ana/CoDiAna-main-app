package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingException;

/**
 *
 * @author Jan Hybs
 */
public interface IGrading {

    void setSettings (GradingSetting setting);



    GradingSetting getSettings ();



    GradingResult grade () throws GradingException;



    GradingType getType ();



    GradingResult getGradingResult ();
}
