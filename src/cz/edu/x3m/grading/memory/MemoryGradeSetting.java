package cz.edu.x3m.grading.memory;

import cz.edu.x3m.grading.GradingSetting;

/**
 *
 * @author Jan Hybs
 */
public class MemoryGradeSetting extends GradingSetting {

    private final int limitMemoryFalling;
    private final int limitMemoryNothing;



    public MemoryGradeSetting (Boolean isManaging, int limitMemoryFalling, int limitMemoryNothing) {
        super (isManaging);
        this.limitMemoryFalling = limitMemoryFalling;
        this.limitMemoryNothing = limitMemoryNothing;
    }



    /**
     * @return the limitMemoryFalling
     */
    public int getLimitMemoryFalling () {
        return limitMemoryFalling;
    }



    /**
     * @return the limitMemoryNothing
     */
    public int getLimitMemoryNothing () {
        return limitMemoryNothing;
    }
}
