package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.structure.AttemptItem;


/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PlagPair implements IPlagPair {

    private final AttemptItem first;
    private final AttemptItem second;
    private final Difference difference;



    public PlagPair (AttemptItem first, AttemptItem second, Difference difference) {
        this.first = first;
        this.second = second;
        this.difference = difference;
    }



    @Override
    public AttemptItem getFirst () {
        return first;
    }



    @Override
    public AttemptItem getSecond () {
        return second;
    }



    @Override
    public Difference getDifference () {
        return difference;
    }
}
