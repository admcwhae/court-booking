/**
 * Allows the creation of a custom exception
 *
 * @author Alex McWhae
 */
public class MyException extends Exception
{
    // mesage to detail what went wrong
    private String message;
    /**
     * Constructor for objects of class MyException
     *
     * @param message the message detailing what caused the exception
     */
    public MyException(String message)
    {
        this.message = message;
    }

    /**
     * Accessor method to get the exception's message
     *
     * @return String the message
     */
    public String getMessage()
    {
        return message;
    }
}
