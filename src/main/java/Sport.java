import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 * This class represents a sport that contains: a name, a usage fee, an insurance fee, and a list of courts for which it can
 * be played on
 *
 * @author Alex McWhae
 */
public class Sport
{
    private String name;
    private double usageFee;
    private double insuranceFee;
    private ArrayList<Court> courts;

    /**
     * Constructor
     *
     * @param name  the name of the sport
     * @param usageFee  the fee to use the court
     * @param insuranceFee  the fee for insurance
     * @param courts    an array of integers that consists of the court numbers for this sport
     */
    public Sport(String name, double usageFee, double insuranceFee, int[] courts)
    {
        this.name = name;
        this.usageFee = usageFee;
        this.insuranceFee = insuranceFee;
        this.courts = new ArrayList<Court>();

        for (int i = 0; i < courts.length; i++)
        {
            this.courts.add(new Court(courts[i])); // creates a Court object for each court id
        }
    }

    /**
     * A mutator method to set the name of the sport 
     *
     * @param name the name of the sport
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * An accessor method to get the name of the sport 
     *
     * @return String the name of the sport
     */
    public String getName()
    {
        return name;
    }

    /**
     * A mutator method to set the sport usage fee 
     *
     * @param usageFee  the fee to use the court
     */
    public void setUsageFee(double fee)
    {
        this.usageFee = fee;
    }

    /**
     * An accessor method to get the sport usage fee 
     *
     * @return double  the fee to use the court
     */
    public double getUsageFee()
    {
        return usageFee;
    }

    /**
     * A mutator method to set the sport insurance fee
     *
     * @param insuranceFee  the fee for insurance
     */
    public void setInsuranceFee(double fee)
    {
        this.insuranceFee = fee;
    }

    /**
     * An accessor method to get the sport insurance fee 
     *
     * @return double  the insurance fee to use the court
     */
    public double getInsuranceFee()
    {
        return insuranceFee;
    }

    /**
     * An accessor method to get the sport courts
     *
     * @return ArrayList<Court>  the array list of courts
     */
    public ArrayList<Court> getCourts()
    {
        return courts;
    }

    /**
     * A mutator method to set the sport courts
     *
     * @param courts  the list of courts used by the sport
     */
    public void setCourts(ArrayList<Court> courts)
    {
        this.courts = courts;
    }

    /**
     * Creates and adds a new court object to the array list
     *
     * @param courtId   the id of the new court Id
     */
    public void addCourt(int courtId)
    {
        courts.add(new Court(courtId));
    }

    /**
     * Gets the court object that matches the given court id
     *
     * @param courtId the id of the court to find
     * @return Court the court that matches the id
     */
    public Court getCourt(int id)
    {
        Court result = null;
        for (Court court : courts)
        {
            if (id == court.getCourtId())
            {
                result = court;
                break;
            }
        }
        return result;
    }

    /**
     * Checks to see if the sport contains a court matching the given court id 
     *
     * @param courtId the id of the court to check
     * @return boolean indicating whether the sport is played on that court
     */
    public boolean hasCourt(int courtId)
    {
        boolean result = false;
        for (Court court : courts)
            if (courtId == court.getCourtId())
            {
                result = true;
                break;
            }
        return result;
    }

    /**
     * Adds a booking for the sport
     *
     * @param memberId the id of the member making the booking
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     * @return int the courtId that the sport was booking on, returns 0 if not booked
     */
    public int addBooking(int memberId, LocalDate date, LocalTime startTime,LocalTime endTime)
    {
        boolean courtAvailable = false;
        Court court = null;
        int i;
        int result = 0;

        //checks to see if a court is available between the given times on the date
        for (i = 0; i < courts.size(); i++)
            if (courts.get(i).checkCourtAvailable(date, startTime, endTime))
            {
                courtAvailable = true;
                break;
            }
        // if available makes the booking
        if (courtAvailable)
        {
            courts.get(i).addBooking(memberId,date, startTime,endTime);
            result = courts.get(i).getCourtId();
        }

        return result;
    }

    /**
     * Adds a booking for the sport when the court Id is already know, for reading from file
     *
     * @param memberId the id of the member making the booking
     * @param courtId the id of the court the booking is to be made
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     */
    public void addBooking(int memberId, int courtId, LocalDate date, LocalTime startTime,LocalTime endTime)
    {
        Court court = getCourt(courtId);
        court.addBooking(memberId, date, startTime, endTime);
    }

    /**
     * Removes a booking for the sport
     *
     * @param memberId the id of the member making the booking
     * @param courtId the id of the court the booking is to be made
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     * @return boolean indicating whether the court was removed successfully
     */
    public boolean removeBooking(int memberId,int courtId,LocalDate date,LocalTime startTime, LocalTime endTime)
    {
        Court court = getCourt(courtId);
        if (court.removeBooking(memberId, date, startTime)) //removes booking, if successful returns true
            return true;
        else
            return false;
    }

    /**
     * Checks to see if a court is available for the sport in the given times
     *
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     * @return boolean indicating whether a court is available
     */
    public boolean hasCourtAvailable(LocalDate date, LocalTime startTime,LocalTime endTime)
    {
        boolean result = false;
        for (int i = 0; i < courts.size(); i++)
            if (courts.get(i).checkCourtAvailable(date, startTime, endTime))
            {
                result = true;
                break;
            }
        return result;
    }

    /**
     * Gets a list of strings that represent a graphic representation of all the available courts of the sport
     *
     * @param date the date of the booking
     * @return ArrayList<String> the lsit of strings containing the information
     */
    public ArrayList<String> getAvailableCourtsGraph(LocalDate date)
    {
        ArrayList<String> result = new ArrayList<String>();

        for (Court court : courts)
            result.add(court.getBookingsGraph(date));

        return result;
    }

    /**
     * Returns all of the data in the class in a String format
     *
     * @return String a string containing the data in the class
     */
    public String toString()
    {
        String courtsString = "";

        for (Court court : courts)
        {
            courtsString = courtsString + " " + court.getCourtId();
        }

        return name + " " + usageFee + " " + insuranceFee + courtsString;
    }
}