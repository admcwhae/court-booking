/**
 * This class represents Basketball, a subclass of Sport, it contains: a name, a usage fee, an insurance fee, and a list
 * of courts for which it can be played as well as a double containing the net height for the sport
 *
 * @author Alex McWhae
 */
public class Basketball extends Sport
{
    private double netHeight;

    /**
     * Constructor, calls the constructor in Sport, and sets the net height
     *
     * @param name  the name of the sport
     * @param usageFee  the fee to use the court
     * @param insuranceFee  the fee for insurance
     * @param courts    an array of integers that consists of the court numbers for this sport
     * @param netHeight a double containing the height of the basketball net
     */
    public Basketball(String name, double usageFee, double insuranceFee, int[] courts, double netHeight)
    {
        super(name, usageFee, insuranceFee, courts);
        this.netHeight = netHeight;
    }

    /**
     * A mutator method that sets the net height
     *
     * @param netHeight the height of the net
     */
    public void setNetHeight(double netHeight)
    {
        this.netHeight = netHeight;
    }

    /**
     * An accessor method that gets the netHeight
     *
     * @return double the net height
     */
    public double getNetHeight()
    {
        return netHeight;
    }

    /**
     * Returns all of the data in the class in a String format
     *
     * @return String a string containing the data in the class
     */
    public String toString()
    {
        return super.toString() + " " + netHeight;
    }

}
