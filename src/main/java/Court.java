import java.util.*;
import java.time.*;
/**
 * This class represents a court, it contains a court id, and list of bookings made for the court
 * and a list of bookings they've made
 *
 * @author Alex McWhae
 */
public class Court
{
    // list of bookings
    private ArrayList<Booking> courtBookings;
    // the court id
    private int courtId;

    /**
     * Constructor
     *
     * @param courtId the id of the court
     */
    public Court(int courtId)
    {
        this.courtId = courtId;
        //creats an empty booking list
        this.courtBookings = new ArrayList<Booking>();
    }

    /**
     * A mutator method to set the court id
     *
     * @param courtId the id of the court
     */
    public void setCourtId(int courtId)
    {
        this.courtId = courtId;
    }

    /**
     * An accessor method to get the court id
     *
     * @return int the id of the court
     */
    public int getCourtId()
    {
        return courtId;
    }

    /**
     * A mutator method to set the court bookings
     *
     * @param courtBookings the list of bookings for the court
     */
    public void setCourtBookings(ArrayList<Booking> courtBookings)
    {
        this.courtBookings = courtBookings;
    }

    /**
     * An accessor method to get the court bookings
     *
     * @return ArrayList<Booking> the list of bookings for the court
     */
    public ArrayList<Booking> getCourtBookings() throws MyException
    {
        if (courtBookings.size() == 0)
        {
            throw new MyException("There are no bookings for that court");
        }

        return courtBookings;
    }

    /**
     * Adds a booking to the court, using the court id
     *
     * @param memberId the id of the member making the booking
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     */
    public void addBooking(int memberId, LocalDate date, LocalTime startTime, LocalTime endTime)
    {
        courtBookings.add(new Booking(date, startTime, endTime, memberId, this.courtId));
    }

    /**
     * Gets a booking with matching member id, date and start time
     *
     * @param memberId the id of the member who made the booking
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @return Booking the matching booking
     */
    public Booking getBooking(int memberId, LocalDate date, LocalTime startTime)
    {
        Booking result = null;
        for (Booking booking : courtBookings)
            if (booking.getMemberId() == memberId)
                if (booking.getDate().equals(date))
                    if (booking.getStartTime().equals(startTime))
                        result = booking;
        return result;
    }

    /**
     * Removes a booking with matching member id, date and start time
     *
     * @param memberId the id of the member who made the booking
     * @param date the date of the booking to remove
     * @param startTime the starting time of the booking
     * @return boolean indicating wheteher removed or not
     */
    public boolean removeBooking(int memberId, LocalDate date, LocalTime startTime)
    {
        Booking booking = null;
        boolean result = false;

        Booking bookingToRemove = getBooking(memberId, date, startTime);

        Iterator<Booking> itr = courtBookings.iterator();
        // loops through, if booking is equal removes it
        while (itr.hasNext())
        {
            booking = itr.next();
            if (booking.equals(bookingToRemove))
            {
                itr.remove();
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks if the court is available on a given date and start time and end time
     *
     * @param date the date of the booking to check
     * @param startTime the starting time of the booking
     * @param endTime the ending time of the booking
     * @return boolean indicating whether the court is available
     */
    public boolean checkCourtAvailable(LocalDate date, LocalTime startTime, LocalTime endTime)
    {
        boolean available = true;
        if (courtBookings.size() == 0)
        {
        }
        else
            for (Booking booking : courtBookings)
            {
                if (booking.getDate().equals(date))
                {
                    LocalTime bookingStartTime = booking.getStartTime();
                    LocalTime bookingEndTime = booking.getEndTime();
                    // if the times fall between the given times then returns false
                    if (!(startTime.isAfter(bookingEndTime) || startTime.isBefore(bookingStartTime)||  startTime.equals(bookingEndTime)))
                        if (!(endTime.isAfter(bookingEndTime) || endTime.isBefore(bookingStartTime) || endTime.equals(bookingStartTime)))
                            available = false;
                }
            }
        return available;
    }

    /**
     * Returns the list of bookings in the future in string format
     *
     * @return ArrayList<String> the list of bookings in string format
     */
    public ArrayList<String> getFutureBookings()
    {
        ArrayList<String> result = new ArrayList<String>();
        for (Booking booking : courtBookings)
        {
            // booking is in the future
            if (booking.getDate().isAfter(LocalDate.now()) || booking.getDate().equals(LocalDate.now()))
                result.add(booking.toString());
        }
        return result;
    }

    /**
     * Returns the list of bookings in string format
     *
     * @return ArrayList<String> the list of bookings in string format
     */
    public ArrayList<String> getBookingsToString()
    {
        ArrayList<String> result = new ArrayList<String>();

        for (Booking booking : courtBookings)
        {
            result.add(booking.toString());
        }
        return result;
    }

    /**
     * Gets all bookings matching the given date and returns them in a string format indicating when the court is available
     *
     * @param date  the date to show
     * @return String a graphical representaiton of the court availability
     */
    public String getBookingsGraph(LocalDate date)
    {
        String result = String.format("| %3d | |",courtId); //sets the court column
        int startTime = 9;
        int endTime = 10;
        boolean courtAvailable;
        // cycles through start and end times in 1 hour increments
        while (endTime < 23)
        {
            // checks to see if court available between given times
            courtAvailable = checkCourtAvailable(date, LocalTime.of(startTime, 0), LocalTime.of(endTime, 0));
            // if booked prints x's otherwise -'s
            if (courtAvailable)
                result += "---|";
            else
                result += "xxx|";
            // increments times
            startTime += 1;
            endTime += 1;
        }

        result += " |";

        return result;
    }
}
