package utility;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.*;
/**
 * Gets user input for a variety of different data types and ensures validity
 *
 * @author Alex McWhae
 * @version 26/4/18
 */
public class IOUtility
{
    public static Scanner scan = new Scanner(System.in);

    /**
     * Gets an integer input
     *
     * @param prompt a string letting the user know what they're entering data for
     * @return int the integer entered by the user
     */
    public static int getIntInput(String prompt)
    {
        System.out.println(prompt);
        while (!scan.hasNextInt())
        {
            System.out.println("Please enter an integer");
            scan.nextLine();
        }
        int integer =scan.nextInt();
        scan.nextLine();
        return integer;
    }

    /**
     * Gets an integer input within a range
     *
     * @param prompt a string letting the user know what they're entering data for
     * @param low the low end of the range
     * @param high the high end of the range
     * @return int the integer entered by the user
     */
    public static int getIntInputRange(String prompt, int low, int high)
    {
        System.out.println(prompt);
        int integer = low - 1;
        while (integer < low || integer > high)
        {
            System.out.println("Please enter an integer between " + low + " and " + high + ".");
            while (!scan.hasNextInt())
            {
                System.out.println("Please enter an integer.");
                scan.nextLine();
            }
            integer =scan.nextInt();
            scan.nextLine();
        }
        return integer;
    }

    /**
     * Gets a string input
     *
     * @param prompt a string letting the user know what they're entering data for
     * @return String the string entered by the user
     */
    public static String getStringInput(String prompt)
    {
        System.out.println(prompt);
        return scan.nextLine();
    }

    /**
     * Gets a date input ensuring that it parses correctly to a LocalDate, note date must be in YYYY-MM-DD format
     *
     * @param prompt a string letting the user know what they're entering data for
     * @return LocalDate the date entered by the user
     */
    public static LocalDate getDateInput(String prompt)
    {
        boolean valid = false;
        String dateString = "";
        LocalDate result = null;
        while (result == null)
        {
            System.out.print(prompt + "(In YYYY-MM-DD format or press ENTER for current date)\n");
            dateString = scan.nextLine();
            if (dateString.isEmpty())
            {
                result = LocalDate.now();
                System.out.println(result);
            }
            else
            {
                try
                {
                    result = LocalDate.parse(dateString);
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
            }
        }
        return result;
    }

    /**
     * Gets a time input, by calling IntInputRange between 0 and 23
     *
     * @param prompt a string letting the user know what they're entering data for
     * @return LocalTime the time entered by the user
     */
    public static LocalTime getTimeInput(String prompt)
    {
        LocalTime result = null;
        int hour = getIntInputRange(prompt, 0, 23);
        result = LocalTime.of(hour,00);
        return result;
    }

}
