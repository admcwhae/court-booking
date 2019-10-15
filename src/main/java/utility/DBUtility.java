package utility;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtility {
    public static Connection connect(){
        Connection conn = null;
        try {
            InputStream stream = DBUtility.class.getResourceAsStream("/config.properties");
            Properties props = new Properties();
            props.load(stream);

            Class.forName("com.mysql.jdbc.Driver");

            String user = props.getProperty("database.user");
            String password = props.getProperty("database.password");
            String url = props.getProperty("database.url");

            conn=DriverManager.getConnection(
                    "jdbc:mysql://" + url + ":3306/court_booking?serverTimezone=Australia/Melbourne",user,password);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return conn;
    }
}
