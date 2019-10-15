/**
 * This class represents Badminton, a subclass of Sport, it contains: a name, a usage fee, an insurance fee, and a list 
 * of courts for which it can be played as well as a boolean indicating whether or not rackets are provided.
 *
 * @author Alex McWhae
 */
public class Badminton extends Sport
{
    private boolean racketsProvided;

    /**
     * Constructor, calls the constructor in Sport, and sets the rackets provided boolean
     *
     * @param name  the name of the sport
     * @param usageFee  the fee to use the court
     * @param insuranceFee  the fee for insurance
     * @param courts    an array of integers that consists of the court numbers for this sport
     * @param racketsProvided   a boolean indicating if rackets are provided
     */
    public Badminton(String name, double usageFee, double insuranceFee, int[] courts, boolean racketsProvided)
    {
        super(name, usageFee, insuranceFee, courts);
        this.racketsProvided = racketsProvided;
    }

    /**
     * A mutator method that sets if rackets are provided
     *
     * @param racketsProvided a boolean indicating if rackets are provided
     */
    public void setRacketsProvided(boolean racketsProvided)
    {
        this.racketsProvided = racketsProvided;
    }

    /**
     * An accessor method that gets the racketsProvided
     *
     * @return boolean a boolean indicating if rackets are provided
     */
    public boolean getRacketsProvided()
    {
        return racketsProvided;
    }

    /**
     * Returns all of the data in the class in a String format
     *
     * @return String a string containing the data in the class
     */
    public String toString()
    {
        return super.toString() + " " + racketsProvided;
    }
}