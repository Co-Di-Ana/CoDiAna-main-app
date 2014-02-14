package cz.edu.x3m.grading;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IGradingResult {

    /**
     * Method returns result as number <0, 1> where 0 means result cannot be worse 1 meaning result
     * is the best.
     *
     * @return number from <0, 1> indicating success
     */
    public double getResult ();



    /**
     * Method returns number from <0, 100 > in percentual units where 0 means result cannot be worse
     * 100 meaning result is the best (100%).
     *
     * @return number from <0, 100> indicating success
     */
    public int getPercent ();
    
    
    /**
     * Method returns wheter is result correct or not, depending on result/percent
     * @return t/f
     */
    public boolean isCorrect ();
}
