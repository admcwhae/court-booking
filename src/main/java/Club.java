import utility.DBUtility;
import utility.FileUtility;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * This class represents a sports club that contains a list of members and offers a list of sports
 *
 * @author Alex McWhae
 */
public class Club
{
    // the club name
    private String name;
    // the club members
    private ArrayList<Member> members;
    // the list of sports offered by the club
    private ArrayList<Sport> sports;

    /**
     * Constructor for club. Loads member, sports and bookings from file on creation
     *
     * @param name the name of the club
     */
    public Club(String name)
    {
        this.name = name;
        members = new ArrayList<Member>();
        sports = new ArrayList<Sport>();
        // gets the club information from file
        try
        {
            getMembersFromDB();
            getSportsFromDB();
            getBookingsFromDB();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    /**
     * A mutator method to set the club name
     *
     * @param name the name of the club
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * An accessor method to get the name of the club
     *
     * @return String the name of the club
     */
    public String getName()
    {
        return name;
    }

    /**
     * An accessor method to ge tthe list of members 
     *
     * @return ArrayList<Member> the list of members
     */
    public ArrayList<Member> getMembers()
    {
        return members;
    }

    /**
     * A mutator method to set the list of members 
     *
     * @param members the list of members
     */
    public void setMembers(ArrayList<Member> members)
    {
        this.members = members;
    }

    /**
     * An accessor method to ge the list of sports
     *
     * @return ArrayList<Sport> the list of sports
     */
    public ArrayList<Sport> getSports()
    {
        return sports;
    }

    /**
     * A mutator method to set the list of sports
     *
     * @param sports the list of sports
     */
    public void setSports(ArrayList<Sport> sports)
    {
        this.sports = sports;
    }

    /**
     * Adds the given member to the club
     *
     * @param member the member to add to the club 
     */
    public void addMember(Member member)
    {
        members.add(member);
    }

    /**
     * Gets the member with the matching member id 
     *
     * @param memberId the member id to find
     * @return Member the member, null if not found
     */
    public Member getMember(int memberId) throws MyException
    {
        Member result = null;
        for (Member member : members)
        {
            if (memberId == member.getMemberId())
            {
                result = member;
                break;
            }
        }

        if (result == null)
            throw new MyException("Member does not exist");

        return result;
    }

    /**
     * Adds the given sport to the club
     *
     * @param sport the sport to add
     */
    public void addSport(Sport sport)
    {
        sports.add(sport);
    }

    /**
     * Gets the sport with the matching string name
     *
     * @param name the string to match
     * @return Sport the sport that matches
     */
    public Sport getSport(String name)
    {
        Sport result = null;
        for (Sport sport : sports)
        {
            if (name.equals(sport.getName()))
            {
                result = sport;
                break;
            }
        }
        return result;
    }

    /**
     * Gets the sport at the given index
     *
     * @param index the index to get
     * @return Sport the sport at the index
     */
    public Sport getSport(int index)
    {
        return sports.get(index);
    }

    /**
     * Returns the name of the sport that has the given court id 
     *
     * @param courtId the court id to look for
     * @return String the name of the sport
     */
    public String sportHasCourt(int courtId) throws MyException
    {
        String result = null;

        for (Sport sport : sports)
            if(sport.hasCourt(courtId))
                result = sport.getName();

        if (result == null)
            throw new MyException("That court does not exist");

        return result;
    }

    /**
     * Adds a booking to the court and member
     *
     * @param memberId the id of the member making the booking
     * @param sportName the name of the sport
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     */
    public void addBooking(int memberId, String sportName, LocalDate date, LocalTime startTime, LocalTime endTime) throws MyException
    {
        Member member = getMember(memberId);
        Sport sport = getSport(sportName);
        // adds the booking in the sport, returning the court id booked
        int courtId = sport.addBooking(memberId, date, startTime, endTime);
        // if court was booked (indicated by non zero) adds booking to member
        if (courtId != 0)
            member.addBooking(sport.getCourt(courtId).getBooking(memberId, date, startTime));

        try{
            Connection conn = DBUtility.connect();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO booking(`memberId`, `courtId`, `date`, `startTime`, `endTime`) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, memberId);
            statement.setInt(2, courtId);
            statement.setDate(3, java.sql.Date.valueOf(date));
            statement.setTime(4, java.sql.Time.valueOf(startTime));
            statement.setTime(5, java.sql.Time.valueOf(endTime));
            statement.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Removes a booking from the court and member
     *
     * @param memberId the id of the member making the booking
     * @param courtId the id of the court to remove
     * @param date the date of the booking
     * @param startTime the starting time of the booking
     * @param endTime the end time of the booking
     */
    public String removeBooking(int memberId, int courtId, String sportName, LocalDate date, LocalTime startTime, LocalTime endTime) throws MyException
    {
        Member member = getMember(memberId);
        Sport sport = getSport(sportName);
        sport.removeBooking(memberId,  courtId,  date, startTime, endTime);

        if (member.removeBooking(date, startTime))
        {
            try{
                Connection conn = DBUtility.connect();
                PreparedStatement statement = conn.prepareStatement("DELETE FROM booking WHERE memberId = ? AND courtId = ? AND date = ? AND startTime = ? AND endTime = ?");
                statement.setInt(1, memberId);
                statement.setInt(2, courtId);
                statement.setDate(3, java.sql.Date.valueOf(date));
                statement.setTime(4, java.sql.Time.valueOf(startTime));
                statement.setTime(5, java.sql.Time.valueOf(endTime));
                statement.executeUpdate();
                conn.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            return "Booking deleted successfully";

        }
        else
            return "Booking could not be found";
    }

    /**
     * Checks that booking date is valid against business rules
     *
     * @param date the date of the attempted booking
     * @param memberId the id of the member making the booking
     * @return boolean the indicating the outcome, true if no exceptions generated
     * @throws MyException if booking invalid
     */
    public boolean validBookingDate(LocalDate date, int memberId) throws MyException
    {
        // bookins cannot be made more that 7 days in advance
        if (date.isAfter(LocalDate.now().plusDays(7)))
            throw new MyException("Bookings can only be made 7 days in advance");
            // bookings can only be made for future dates
        else if (date.isBefore(LocalDate.now()))
            throw new MyException("Bookings can only be made for future dates");
            // a member is only allowed one booking per day
        else if (getMember(memberId).hasBooking(date))
            throw new MyException("Only one booking can be made per day per member");
        return true;
    }

    /**
     * Checks that booking start time is valid against business rules
     *
     * @param time the start time of the attempted booking
     * @return boolean the indicating the outcome, true if no exceptions generated
     * @throws MyException if booking invalid
     */
    public boolean validBookingStartTime(LocalTime time) throws MyException
    {
        // time must not be before 9am or after 9pm
        if (time.isBefore(LocalTime.of(9, 0)))
            throw new MyException("Bookings can only be made after 09:00");
        else if (time.isAfter(LocalTime.of(21, 0)))
            throw new MyException("Bookings cannot be made after 21:00");
        return true;
    }

    /**
     * Checks that booking end time is valid against business rules
     *
     * @param startTime the start time of the attempted booking
     * @param endTime the end time of the attempted booking
     * @param sportName the name of the sport booking for
     * @return boolean the indicating the outcome, true if no exceptions generated
     * @throws MyException if booking invalid
     */
    public boolean validBookingEndTime(LocalTime startTime, LocalTime endTime, String sportName) throws MyException
    {
        // end time has to be after start time
        if (endTime.isBefore(startTime) || endTime.equals(startTime))
            throw new MyException("The end time has to be after the start time.");
            // bookings cannot end after 10pm
        else if (endTime.isAfter(LocalTime.of(22, 0)))
            throw new MyException("Bookings have to end on or before 22:00.");
            // max booking of 3 hours for basketball or 2 for badminton
        else if (sportName.equals("Basketball") && startTime.plusHours(3).isBefore(endTime))
            throw new MyException("Bookings for Basketball can't be longer than 3 hours.");
        else if (sportName.equals("Badminton") && startTime.plusHours(2).isBefore(endTime))
            throw new MyException("Bookings for Badminton can't be longer than 2 hours.");
        return true;
    }

    /**
     * Checks that member is a financial member
     *
     * @param memberId the id of the member
     * @return String the indicating the outcome
     */
    public String validMemberFinancial(int memberId) throws MyException
    {
        Member member = getMember(memberId);
        String result = "";
        // if not financial member
        if (!member.getFinancial())
            throw new MyException("That member is not a financial member and therefore cannot make a booking");
        return result;
    }

    /**
     * Gets the member information from database, and adds to list of members
     */
    public void getMembersFromDB()
    {
        try
        {
            Connection conn = DBUtility.connect();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM member");
            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                boolean financial = rs.getBoolean(3);
                ArrayList<String> sportsPlayed = new ArrayList<String>();

                PreparedStatement sportsStmt = conn.prepareStatement("SELECT sp.name FROM sport AS sp INNER JOIN participant AS part ON part.sportId = sp.id INNER JOIN member AS mem ON part.memberId = mem.id WHERE mem.id = ?");
                sportsStmt.setInt(1, id);
                ResultSet sportsRs = sportsStmt.executeQuery();
                while(sportsRs.next()) {
                    String sportName = sportsRs.getString(1);
                    sportsPlayed.add(sportName);
                }

                addMember(new Member(id, name, financial, sportsPlayed));
            }
            conn.close();
        }
        catch(Exception e) {
        }
    }

    /**
     * Gets the sports information from database and adds it to list of sports
     */
    public void getSportsFromDB()
    {
        try
        {
            Connection conn = DBUtility.connect();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM sport");
            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double usage = rs.getDouble(3);
                double insurance = rs.getDouble(4);

                ArrayList<Integer> courtsList = new ArrayList<Integer>();
                PreparedStatement courtsStmt = conn.prepareStatement("SELECT courtNumber FROM court WHERE sportId = ?");
                courtsStmt.setInt(1, id);
                ResultSet courtsRs = courtsStmt.executeQuery();
                while(courtsRs.next()) {
                    int courtNumber = courtsRs.getInt(1);
                    courtsList.add(courtNumber);
                }

                int[] courtsArray = new int[courtsList.size()];
                for(int i = 0; i < courtsList.size(); i++) {
                    courtsArray[i] = courtsList.get(i).intValue();
                }

                Sport temp = null;
                if (name.toLowerCase().equals("basketball"))
                    temp = new Basketball(name, usage, insurance,courtsArray, 3.05);
                else if (name.toLowerCase().equals("badminton"))
                    temp = new Badminton(name, usage, insurance, courtsArray, true);
                else
                    temp = new Sport(name, usage, insurance, courtsArray);

                addSport(temp);
            }
            conn.close();
        }
        catch(Exception e) {
        }
    }

    /**
     * Gets the booking information from database and adds it to list of bookings
     */
    public void getBookingsFromDB() throws MyException
    {
        try
        {
            Connection conn = DBUtility.connect();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM booking");
            while(rs.next()) {
                int id = rs.getInt(1);
                int memberId = rs.getInt(2);
                int courtId = rs.getInt(3);
                LocalDate date = rs.getDate(4).toLocalDate();
                LocalTime startTime = rs.getTime(5).toLocalTime();
                LocalTime endTime = rs.getTime(6).toLocalTime();

                Sport tempSport = null;
                Member tempMember = null;
                // gets the sport with the enter court id
                for (Sport sport : sports)
                {
                    if (sport.getCourt(courtId) != null)
                        tempSport = sport;
                }
                // gets the member with the member id
                tempMember = getMember(memberId);
                // adds bookings to sport and member
                tempSport.addBooking(memberId, courtId, date, startTime, endTime);
                tempMember.addBooking(tempSport.getCourt(courtId).getBooking(memberId, date, startTime));
            }

            conn.close();
        }
        catch(Exception e) {
        }

    }

    /**
     * Gets the members in the club as a list of strings
     *
     * @return ArrayList<String> the list of members in string format
     */
    public ArrayList<String> getMembersToString()
    {
        ArrayList<String> result = new ArrayList<String>();
        for (Member member : members)
        {
            result.add(member.toString());
        }
        return result;
    }

    /**
     * Gets the sports in the club as a list of strings
     *
     * @return ArrayList<String> the list of sports in string format
     */
    public ArrayList<String> getSportsToString()
    {
        ArrayList<String> result = new ArrayList<>();
        for (Sport sport : sports)
        {
            result.add(sport.toString());
        }
        return result;
    }

    /**
     * Gets the sports names in the club as a list of strings with index
     *
     * @return ArrayList<String> the list of sports names with index in the club
     */
    public ArrayList<String> getSportsNamesToString()
    {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < sports.size(); i++)
        {
            result.add((i + 1) + " - " + sports.get(i).getName());
        }
        return result;
    }
}