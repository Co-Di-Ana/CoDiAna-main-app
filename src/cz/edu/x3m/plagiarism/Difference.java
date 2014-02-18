package cz.edu.x3m.plagiarism;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Difference {

    private double value;
    private double maxim;



    /**
     * Create difference object from given maximum and value numbers
     *
     * @param value
     * @param max
     */
    public Difference (double value, double max) {
        this.maxim = max;
        this.value = value > max ? max : value;
    }



    /**
     * Create difference object from set of given differences.
     *
     * @param items
     */
    public Difference (Difference... items) {
        value = 0;
        maxim = 0;
        for (Difference d : items) {
            value += d.getValue ();
            maxim += d.getMax ();
        }
    }



    /**
     * Method creates copy of this object and performs balance, meaning that previous maximum will
     * be set to given maximum, and value will be adjusted.
     *
     * @param maximum new maximum
     * @return copy of this object with adjusted maximum
     */
    public Difference balance (int maximum) {
        if (maxim == 0)
            return new Difference (0, maximum);
        double balance = maximum / maxim;
        return new Difference (value * balance, maximum);
    }



    /**
     * Returns new difference conzisting of sum of given and this object
     *
     * @param difference
     */
    public Difference add (Difference difference) {
        if (difference == null)
            return this;
        return new Difference (value + difference.getValue (), maxim + difference.getMax ());
    }



    /**
     * Returns value how much can two object differ.
     */
    public double getMax () {
        return maxim;
    }



    /**
     * Returns difference value, number which represents how much are two object not the same.
     */
    public double getValue () {
        return value;
    }



    /**
     * Returns probability defined by this difference, probability is in range <0, 1>
     * where 1 means objects are identical.
     */
    public double getIdenticalLikelihood () {
        return (maxim - value) / maxim;
    }



    @Override
    public String toString () {
        return String.format ("[%1.2f of (%1.2f)]", value, maxim);
    }



    public boolean hasLowerDifference (Difference that) {
        if (that == null)
            return true;
        return this.getIdenticalLikelihood () > that.getIdenticalLikelihood ();
    }



    public static Difference empty () {
        return new Difference (0, 0);
    }



    public boolean isIdentical () {
        return getIdenticalLikelihood () == 1;
    }
}
