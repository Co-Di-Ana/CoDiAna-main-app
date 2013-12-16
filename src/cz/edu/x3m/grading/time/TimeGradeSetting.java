package cz.edu.x3m.grading.time;

import cz.edu.x3m.grading.GradingSetting;

/**
 *
 * @author Jan Hybs
 */
public class TimeGradeSetting extends GradingSetting {

    private final int limitTimeFalling;
    private final int limitTimeNothing;



    public TimeGradeSetting (Boolean isManaging, int limitTimeFalling, int limitTimeNothing) {
        super (isManaging);
        this.limitTimeFalling = limitTimeFalling;
        this.limitTimeNothing = limitTimeNothing;
    }



    /**
     * @return the limitTimeFalling
     */
    public int getLimitTimeFalling () {
        return limitTimeFalling;
    }



    /**
     * @return the limitTimeNothing
     */
    public int getLimitTimeNothing () {
        return limitTimeNothing;
    }
}
