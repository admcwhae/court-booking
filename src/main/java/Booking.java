import java.time.*;
/**
 * A class that represents a booking by a member for a court on a date and time, ending at a later time
 *
 * @author Alex McWhae
 */
public class Booking
{
    // the date of the booking
    private LocalDate date;
    // the start time of the booking
    private LocalTime startTime;
    // the end time of the booking
    private LocalTime endTime;
    // the member id of the member that booked
    private int memberId;
    // the court id for the court
    private int courtId;
    /**
     * Constructor
     *
     * @param date the date of the booking
     * @param startTime the time the booking starts
     * @param endTime the time the booking ends
     * @param memberId the id of the member making the booking
     * @param courtId the id of the court the booking is being made on
     */
    public Booking(LocalDate date, LocalTime startTime, LocalTime endTime,int memberId,int courtId)
    {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memberId = memberId;
        this.courtId = courtId;
    }

    /**
     * A mutator method to set the court id
     *
     * @param courtId the id of the court that is booked
     */
    public void setCourtId(int courtId)
    {
        this.courtId = courtId;
    }

    /**
     * A mutator method to set the member id
     *
     * @param memberId the id of the member the booking is for
     */
    public void setMemberId(int memberId)
    {
        this.memberId = memberId;
    }

    /**
     * A mutator method to set the date
     *
     * @param date the date of the booking
     */
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    /**
     * A mutator method to set the start time
     *
     * @param startTime the time the booking starts
     */
    public void setStartTime(LocalTime startTime)
    {
        this.startTime = startTime;
    }

    /**
     * A mutator method to set the end time
     *
     * @param endTime the time the booking ends
     */
    public void setEndTime(LocalTime endTime)
    {
        this.endTime = endTime;
    }

    /**
     * An accessor method to get the court id
     *
     * @return int the courtID
     */
    public int getCourtId()
    {
        return courtId;
    }

    /**
     * An accessor method to get the member id
     *
     * @return int the memberId
     */
    public int getMemberId()
    {
        return memberId;
    }

    /**
     * An accessor method to get the booking date
     *
     * @return LocalDate the booking date
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     * An accessor method to get the booking start time
     *
     * @return LocalTime the booking start time
     */
    public LocalTime getStartTime()
    {
        return startTime;
    }

    /**
     * An accessor method to get the booking end time
     *
     * @return LocalTime the booking end time
     */
    public LocalTime getEndTime()
    {
        return endTime;
    }

    /**
     * A method that returns all the fields of the booking in a String format
     *
     * @return String the fields of booking in string form
     */
    public String toString()
    {
        return String.format("| %3d | %5s | %s | %s | %s |",memberId, courtId, date, startTime, endTime);
    }

    /**
     * A method that returns all the fields of the booking in a String format that is in the correct format for writing to file
     *
     * @return String the fields of booking in string form
     */
    public String toStringForFileWrite()
    {
        return memberId + "," + courtId + "," + date.getYear() + "," + date.getMonthValue() + "," +date.getDayOfMonth() + "," + startTime.getHour() + "," + endTime.getHour();
    }
}
