import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
/**
 * This class represents a member at a sports club, it contains a name, member id, financial status, a list of sports the play
 * and a list of bookings they've made
 *
 * @author Alex McWhae
 */
public class Member
{
    // the members name
    private String name;
    // the members id
    private int memberId;
    // indicates financial status, (can they make bookings)
    private boolean financial;
    // a list of the sports the member plays
    private ArrayList<String> sportsPlayed;
    // a list of bookings made by the member
    private ArrayList<Booking> bookings;

    /**
     * Constructor for Member
     *
     * @param memberId the id of the member
     * @param name  the name of the member
     * @param financial financial status of the member
     * @param sportsPlayed list of sports played by the member
     */
    public Member(int memberId, String name, boolean financial, ArrayList<String> sportsPlayed)
    {
        this.memberId = memberId;
        this.name = name;
        this.financial = financial;
        this.sportsPlayed = sportsPlayed;
        this.bookings = new ArrayList<Booking>();
    }

    /**
     * A mutator method to set the member name
     *
     * @param name  the name of the member
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * An accessor method to get the member name
     *
     * @return String the name of the member
     */
    public String getName()
    {
        return name;
    }

    /**
     * A mutator method to set the member id
     *
     * @param memberId the id of the member
     */
    public void setMemberId(int memberId)
    {
        this.memberId = memberId;
    }

    /**
     * An accessor method to get the member name
     *
     * @return int the id of the member
     */
    public int getMemberId()
    {
        return memberId;
    }

    /**
     * A mutator method to set the member financial status
     *
     * @param financial financial status of the member
     */
    public void setFinancial(boolean financial)
    {
        this.financial = financial;
    }

    /**
     * An accessor method to get the member financial status
     *
     * @return boolean financial status of the member
     */
    public boolean getFinancial()
    {
        return financial;
    }

    /**
     * A mutator method to set the member sports played
     *
     * @param sportsPlayed list of sports played by the member
     */
    public void setSportsPlayed(ArrayList<String> sportsPlayed)
    {
        this.sportsPlayed = sportsPlayed;
    }

    /**
     * An accessor method to get the member sports played
     *
     * @return ArrayList<String> list of sports played by the member
     */
    public ArrayList<String> getSportsPlayed()
    {
        return sportsPlayed;
    }

    /**
     * A mutator method to set the bookings made by member
     *
     * @param bookings list of bookings made by member
     */
    public void setBookings(ArrayList<Booking> bookings)
    {
        this.bookings = bookings;
    }

    /**
     * An accessor method to get the bookings made by member
     *
     * @return ArrayList<Booking> list of bookings made by member
     */
    public ArrayList<Booking> getBookings() throws MyException
    {
        if (bookings.size() == 0)
        {
            throw new MyException("There are no bookings for that member");
        }

        return bookings;
    }

    /**
     * Returns the sport at the given index
     *
     * @return String   sport at the index
     */
    public String getSport(int index)
    {
        return sportsPlayed.get(index);
    }

    /**
     * Checks to see if the member plays the given sport name
     *
     * @param sportName the name of the sport to look for
     * @return boolean  boolean indicating whether they play the sport
     */
    public boolean playsSport(String sportName)
    {
        boolean result = false;

        for (String sport : sportsPlayed)
        {
            if (sportName.equals(sport))
                result = true;
        }
        return result;
    }

    /**
     * Returns the list sports in a form that is printable by the user interface
     * (index - sportName)
     *
     * @return ArrayList<String> the list of strings containing the sports the user plays
     */
    public ArrayList<String> sportsToStrings()
    {
        ArrayList<String> returnStrings = new ArrayList<String>();
        returnStrings.add("Member " + memberId + " plays: ");
        for (int i = 0; i < sportsPlayed.size(); i++)
        {
            returnStrings.add((i + 1) + " - " + sportsPlayed.get(i));
        }
        return returnStrings;
    }

    /**
     * Creates and adds a booking to the member
     *
     * @param courtId the court id to make the booking on
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     */
    public void addBooking (int courtId, LocalDate date, LocalTime startTime, LocalTime endTime)
    {
        //creates the booking object using the member id as well
        Booking booking = new Booking(date, startTime, endTime, this.memberId, courtId);
        bookings.add(booking); //adds it to list
    }

    /**
     * Adds a booking to the member
     *
     * @param Booking booking to add
     */
    public void addBooking(Booking booking)
    {
        bookings.add(booking);
    }

    /**
     * Checks to see if a member has a booking on the given date
     *
     * @param date the date to look for a booking
     * @return boolean  indicating whether a booking is already made on the date
     */
    public boolean hasBooking(LocalDate date)
    {
        for (Booking booking : bookings)
            if(booking.getDate().equals(date))
                return true;
        return false;
    }

    /**
     * Returns the booking for a member on the given date
     *
     * @param date the date to get the booking
     * @return Booking the booking on the date, null if none found
     */
    public Booking getBooking(LocalDate date)
    {
        for (Booking booking : bookings)
            if(booking.getDate().equals(date))
                return booking;
        return null;
    }

    /**
     * Removes the booking from the member that matches the date, start time
     *
     * @param date the date of the booking to remove
     * @param startTime the starting time of the booking to remove
     * @return boolean indiciating whether booking was removed
     */
    public boolean removeBooking(LocalDate date, LocalTime startTime)
    {
        Booking booking = null;
        boolean result = false;

        Iterator<Booking> itr = bookings.iterator();

        while (itr.hasNext())
        {
            booking = itr.next();
            if (date.equals(booking.getDate()))
                if (startTime.equals(booking.getStartTime()))
                {
                    itr.remove();
                    result = true;
                    break;
                }
        }
        return result;
    }

    /**
     * Writes each booking to a string and saves the bookings in a list of strings
     *
     * @return ArrayList<String> the list of the bookings in string format     
     */
    public ArrayList<String> getBookingsToString()
    {
        ArrayList<String> result = new ArrayList<String>();

        for (Booking booking : bookings)
        {
            result.add(booking.toString());
        }
        return result;
    }

    /**
     * Gets a list of bookings in string format for bookins that are today or in the future
     *
     * @return ArrayList<String> the list of the bookings in the next week in string format 
     */
    public ArrayList<String> getBookingsInNextWeek()
    {
        ArrayList<String> result = new ArrayList<String>();
        for (Booking booking : bookings)
        {
            // checks if booking is a future booking
            if (booking.getDate().isAfter(LocalDate.now()) || booking.getDate().equals(LocalDate.now()))
                result.add(booking.toString());
        }
        return result;
    }

    /**
     * Writes each booking to a string and saves the bookings in a list of strings in the correct format for saving to file
     *
     * @return ArrayList<String> the list of the bookings in string format 
     */
    public ArrayList<String> getBookingsForFileWrite()
    {
        ArrayList<String> bookingStrings = new ArrayList<String>();

        for (Booking booking : bookings)
        {
            bookingStrings.add(booking.toStringForFileWrite());
        }

        return bookingStrings;
    }
}