package sample;

import java.sql.*;


public class DBConnection {
    public Connection databaseLink;

    private String pword="Whoiam@123";

    public Connection Connect()
    {
        try {
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/pmsdb","root",pword);
            System.out.println("Success");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return databaseLink;
    }


}
