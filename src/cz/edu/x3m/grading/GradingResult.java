package cz.edu.x3m.grading;

/**
 *
 * @author Jan Hybs
 */
abstract public class GradingResult implements IGradingResult {

    public static final double MAX = 1.0;
    public static final double MIN = 0.0;
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



    @Override
    public boolean isWrong () {
        return getPercent () <= 0;
    }



    @Override
    public boolean isCorrect () {
        return getPercent () >= 100;
    }
}
