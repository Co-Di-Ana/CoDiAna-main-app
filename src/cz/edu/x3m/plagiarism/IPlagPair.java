package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.structure.AttemptItem;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IPlagPair {

    /**
     * @return the first
     */
    public AttemptItem getFirst ();



    /**
     * @return the second
     */
    public AttemptItem getSecond ();



    /**
     * @return the difference
     */
    public Difference getDifference ();
}
