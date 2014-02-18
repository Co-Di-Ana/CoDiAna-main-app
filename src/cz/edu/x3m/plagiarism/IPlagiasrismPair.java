package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.data.ISolution;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IPlagiasrismPair {

    /**
     * @return the first
     */
    public ISolution getFirst ();



    /**
     * @return the second
     */
    public ISolution getSecond ();



    /**
     * @return the difference
     */
    public Difference getDifference ();
}
