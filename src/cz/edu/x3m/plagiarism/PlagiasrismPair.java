package cz.edu.x3m.plagiarism;

import cz.edu.x3m.database.data.ISolution;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PlagiasrismPair implements IPlagiasrismPair {

    private final ISolution first;
    private final ISolution second;
    private final Difference difference;



    public PlagiasrismPair (ISolution first, ISolution second, Difference difference) {
        this.first = first;
        this.second = second;
        this.difference = difference;
    }



    @Override
    public ISolution getFirst () {
        return first;
    }



    @Override
    public ISolution getSecond () {
        return second;
    }



    @Override
    public Difference getDifference () {
        return difference;
    }
}
