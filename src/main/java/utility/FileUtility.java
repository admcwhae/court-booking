package utility;

import java.util.*;
import java.io.*;
/**
 * A class which contains methods for reading and writing strings to files
 *
 * @author Alex McWhae
 * @version 26/4/18
 */
public class FileUtility
{
    /**
     * Reads data from a file and saves it line by line to an array list of strings, ignoring and comments indicated by //
     *
     * @param   fileName            a string corresponding to the file name of the file
     * @return  ArrayList<String>   An array list of Strings that contains the data read from the file
     */
    public static ArrayList<String> readFromFile(String fileName) throws IOException, FileNotFoundException
    {
        ArrayList<String> data = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String temp = in.readLine();
        while (temp != null && !temp.isEmpty())
        {
            if (!temp.substring(0,2).equals("//"))
            {
                data.add(temp);
            }
            temp = in.readLine();
        }
        in.close();
        return data;
    }

    /**
     * Writes an array of strings to file
     *
     * @param   fileName  a string corresponding to the file name of the file
     * @paran   strings   An array list of Strings that contains the data to write to the file
     */
    public static void writeToFile(String fileName, ArrayList<String> strings) throws IOException
    {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName));
        for(String str : strings)
        {
            pw.println(str);
        }
        pw.close();
    }

}
