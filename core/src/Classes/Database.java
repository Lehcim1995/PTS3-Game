package Classes;

import java.sql.*;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Database
{

    public java.sql.Connection myConn;
    Statement myStmt;
    ResultSet myRs;

    public Database() throws SQLException
    {
	    System.out.println("Database made");
    }

    public boolean Connect() throws ClassNotFoundException
    {

        Class.forName("oracle.jdbc.driver.OracleDriver");

        boolean success = false;

        try
        {
            myConn = DriverManager.getConnection("jdbc:oracle:thin:@//fhictora01.fhict.local:1521/fhictora", "dbi310866", "O4g03ym3r8");
        }
        catch(SQLException ex)
        {
            System.out.println("Connection failed");
        }

        if(myConn != null)
        {
            success = true;
        }
        return success;
    }
    // query =  select * from \"User\"
    public ResultSet GetQuery(String query)
    {
        try
        {
            myStmt = myConn.createStatement();
            return myRs = myStmt.executeQuery(query);
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    // query =  select * from \"User\"
    public ResultSet GetQuery(PreparedStatement query)
    {
        try
        {
            return query.executeQuery();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public boolean InsertQuery(String query)
    {
        try
        {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);

            return true;
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean deleteQuery(String query)
    {
        boolean success = false;
        try
        {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);

            success = true;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        return success;
    }

    public boolean InsertQuery(PreparedStatement query) throws SQLException
    {
        try
        {
            query.executeUpdate();

            return true;
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public void Close()
    {
        try
        {
            if (myRs != null)
            {
                myRs.close();
            }
            if (myStmt != null)
            {
                myStmt.close();
            }
            if (myConn != null)
            {
                myConn.close();
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

}

