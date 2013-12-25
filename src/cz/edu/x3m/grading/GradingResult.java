package cz.edu.x3m.grading;

/**
 *
 * @author Jan Hybs
 */
abstract public class GradingResult implements IGradingResult {

    protected final double result;



    public GradingResult (double result) {
        this.result = Double.isNaN (result) ? 0 : result;
    }



    @Override
    public double getResult () {
        return result;
    }



    @Override
    public int getPercent () {
        return (int) (getResult () * 100);
    }
}
