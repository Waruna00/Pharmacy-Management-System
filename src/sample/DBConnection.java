package sample;

import java.sql.*;


public class DBConnection {

    public Connection databaseLink;

    public Connection Connect()
    {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/pmsdb","root","mamatharindu");
            System.out.println("Success");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return databaseLink;
    }


}
