import java.util.*;
/**
 * Starts the program, instaniates the Club class and runs the user interface
 *
 * @author Alex McWhae
 */
public class Start
{
    public static void main(String[] args)
    {
        Club sportsClub = new Club("Sports Club");

        GUI guiApp = new GUI(sportsClub);
        guiApp.run();

    }
}
