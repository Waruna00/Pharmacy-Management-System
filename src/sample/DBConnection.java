package sample;

import java.sql.*;


public class DBConnection {
    public Connection databaseLink;

    public Connection Connect()
    {
        try {
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/pmsdb","root","Whoiam@123");
            System.out.println("Success");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return databaseLink;
    }


}
