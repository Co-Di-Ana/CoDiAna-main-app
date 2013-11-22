package cz.edu.x3m.grading;

/**
 *
 * @author Jan Hybs
 */
abstract public class GradingSetting {

    private Boolean isManaging;



    public GradingSetting (Boolean isManaging) {
        this.isManaging = isManaging;
    }



    public Boolean isManaging () {
        return isManaging;
    }
}
