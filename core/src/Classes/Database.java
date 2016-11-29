

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 *
 * @author michel
 */
public class Database {

    //"jdbc:sqlserver:mssql.fhict.local;databaseName=dbi327759;user=dbi327759;password=Mssql"

    private final String serverNameSchool = "";
    private final String mydatabaseSchool = "";
    private final String usernameSchool = "";
    private final String passwordSchool = "";

    private final String url = "jdbc:sqlserver://" + serverNameSchool + ";user=" + usernameSchool + ";password=" + passwordSchool + ";database=" + mydatabaseSchool;

    private Database()
    {
        try
        {
            if (DatabaseConnection(url))
            {
                System.out.println("School db connected");
            } else
            {
                System.out.println("Cannot connect to any database");
            }
        } catch (SQLException ex)
        {
            System.out.println("SQLError in Constructor : " + ex);
        }
    }

    private static Database instance;

    /**
     *
     * @return
     */
    public static Database InstanceGet()
    {
        return instance == null ? instance = new Database() : instance; // kan helaas niet kleiner :'(
    }

    /**
     * Converts a date to timestamp
     *
     * @param date the date for converting
     * @return timestamp
     */
    private Timestamp DateToTimestamp(Date date)
    {
        return new Timestamp(date.getTime());
    }

    //<editor-fold defaultstate="collapsed" desc=" Raw DbFunctions ">
    /**
     * inteface to use as a delegete
     *
     * @param <T> The generic of the interface
     */
    interface DatabaseReturn<T> {

        /**
         *
         * @param set the resultset from the query
         * @return returns whatever you want
         * @throws SQLException
         */
        T ReturnType(ResultSet set) throws SQLException;
    }

    /**
     * Makes a save SQL statement and executes it
     *
     * @param sql The query, use an "?" at the place of a input. Like this:
     * INSERT INTO
     * [dbo].[Member]([Username],[Firstname],[Birthdate],[HashedPassword],[StateID],[HasAccess],[Lastname],[Email],[Address],[PhoneNumber],[Job])
     * VALUES(?,?,?,?,?,?,?,?,?,?,?);
     * @param Arguments The arguments correspont to same questionmark.
     * @return
     * @throws SQLException
     */
    public Integer SetDatabase(String sql, Object... Arguments)
    {
        Connection conn = null;
        PreparedStatement psta = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            psta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int i = 0;
            for (Object obj : Arguments)
            {
                i++;

                if (obj != null && obj.getClass() == Date.class) // when the argument is a date then convert to a timestamp
                {
                    psta.setTimestamp(i, DateToTimestamp((Date) obj));
                } else if (obj != null && obj.getClass() == String.class) // when the argument is a string then escap all html4 stuff
                {
                    psta.setObject(i, escapeHtml4((String) obj));
                } else
                {
                    psta.setObject(i, obj);
                }
            }

            psta.executeUpdate();
            rs = psta.getGeneratedKeys();
            if (rs != null)
            {
                if (rs.next())
                {
                    if (rs.getInt(1) == 0)
                    { //maybe errors

                        return -1;
                    }
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e)
        {
            System.out.println("SQL Error " + e.getMessage());
            System.out.println("SQL Error " + e.getErrorCode());
            return -1;
        } catch (ClassNotFoundException e)
        {
            System.out.println("Class Error " + e.getMessage());
            return -1;
        } finally
        {
            if (conn != null)
            {
                //close and commit
                System.out.println("commit " + sql);
                try
                {
                    conn.commit();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if (psta != null)
            {
                try
                {
                    psta.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * reusable database function
     *
     * @param sql
     * @param returnfunction
     * @return
     * @throws SQLException
     */
    private <T> ArrayList<T> GetDatabase(String sql, DatabaseReturn<T> returnfunction, Object... Arguments) throws SQLException
    {
        ArrayList<T> objList = new ArrayList<T>();

        Connection conn = null;
        Statement sta = null;
        PreparedStatement psta = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            psta = conn.prepareStatement(sql);

            int i = 0;
            for (Object obj : Arguments)
            {
                i++;

                if (obj != null && obj.getClass() == Date.class) // when the argument is a date then convert to a timestamp
                {
                    psta.setTimestamp(i, DateToTimestamp((Date) obj));
                } else if (obj != null && obj.getClass() == String.class) // when the argument is a string then escap all html4 stuff
                {
                    psta.setObject(i, escapeHtml4((String) obj));
                } else
                {
                    psta.setObject(i, obj);
                }
            }

            rs = psta.executeQuery();

            while (rs.next())
            {
                objList.add(returnfunction.ReturnType(rs));
            }
        } catch (SQLException e)
        {
            System.out.println("SQL Error " + e.getMessage());
        } catch (ClassNotFoundException e)
        {
            System.out.println("Class Error " + e.getMessage());
        } finally
        {
            if (conn != null)
            {
                conn.close();
            }

            if (sta != null)
            {
                sta.close();
            }

            if (rs != null)
            {
                rs.close();
            }
        }

        return objList;
    }

    private boolean DatabaseConnection(String connection) throws SQLException
    {
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(connection);
            sta = conn.createStatement();
            if (conn == null)
            {
                return false;
            }
            return !conn.isClosed();
        } catch (SQLException e)
        {
            System.out.println("SQL Error " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e)
        {
            System.out.println("Class Error " + e.getMessage());
            return false;
        } finally
        {
            if (conn != null)
            {
                conn.close();
            }

            if (sta != null)
            {
                sta.close();
            }

            if (rs != null)
            {
                rs.close();
            }
        }
    }

//    public getAllusers()
//    {
//        String query = "Select ? from ?";
//
//        GetDatabase(query, user);
//    }

    public ArrayList<User> LogIn(String query, List<Object> arguments) {
        try
        {
            DatabaseReturn<User> user = (set) ->
            {
                String name = set.getString("username");
                String email = set.getString("email");
                int kills = set.getInt("kills");
                int deaths = set.getInt("deaths");
                int shotshit = set.getInt("shotshit");
                int shots = set.getInt("shots");
                int matchesplayed = set.getInt("matchesplayed");
                int matcheswon = set.getInt("matcheswon");
                int matcheslost = set.getInt("matcheslost");
                int isBanned = set.getInt("isbanned");

                return new User(name, email, kills, deaths, shotshit, shots, matchesplayed, matcheswon, matcheslost, isBanned);
            };

            return GetDatabase(query, user, arguments);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    // </editor-fold>
}


//package Classes;
//
//import com.mysql.jdbc.PreparedStatement;
//
//import java.sql.*;
//import java.util.ArrayList;
//
///**
// * Created by Jasper on 19-11-2016.
// */
//public class Database
//{
//    //"jdbc:sqlserver:mssql.fhict.local;databaseName=dbi327759;user=dbi327759;password=Mssql"
//
//    private final String serverNameSchool = "";
//    private final String mydatabaseSchool = "";
//    private final String usernameSchool = "";
//    private final String passwordSchool = "";
//
//    private final String urlSchool = "jdbc:sqlserver://" + serverNameSchool + ";user=" + usernameSchool + ";password=" + passwordSchool + ";database=" + mydatabaseSchool;
//
//    private static Database instance;
//
////    public java.sql.Connection myConn;
////    Statement myStmt;
////    ResultSet myRs;
//
//    private Database()
//    {
//        try
//        {
//            if (DatabaseConnection(urlSchool))
//            {
//                System.out.println("School db connected");
//            } else
//            {
//                System.out.println("Cannot connect to any database");
//            }
//        } catch (SQLException ex)
//        {
//            System.out.println("SQLError in Constructor : " + ex);
//        }
//    }
//
//    /**
//     *
//     * @return instance of Database
//     */
//    public static Database InstanceGet()
//    {
//        return instance == null ? instance = new Database() : instance; // kan helaas niet kleiner :'(
//    }
//
//    /**
//     * Converts a date to timestamp
//     *
//     * @param date the date for converting
//     * @return timestamp
//     */
//    private Timestamp DateToTimestamp(Date date)
//    {
//        return new Timestamp(date.getTime());
//    }
//
//    //<editor-fold defaultstate="collapsed" desc=" Raw DbFunctions ">
//    /**
//     * inteface to use as a delegete
//     *
//     * @param <T> The generic of the interface
//     */
//    interface DatabaseReturn<T> {
//
//        /**
//         *
//         * @param set the resultset from the query
//         * @return returns whatever you want
//         * @throws SQLException
//         */
//        T ReturnType(ResultSet set) throws SQLException;
//    }
//
//    /**
//     * Makes a save SQL statement and executes it
//     *
//     * @param sql The query, use an "?" at the place of a input. Like this:
//     * INSERT INTO
//     * [dbo].[Member]([Username],[Firstname],[Birthdate],[HashedPassword],[StateID],[HasAccess],[Lastname],[Email],[Address],[PhoneNumber],[Job])
//     * VALUES(?,?,?,?,?,?,?,?,?,?,?);
//     * @param Arguments The arguments correspont to same questionmark.
//     * @return
//     * @throws SQLException
//     */
//    private Integer SetDatabase(String sql, Object... Arguments) throws SQLException
//    {
//        com.mysql.jdbc.Connection conn = null;
////        java.sql.Connection conn = null;
////        Connection conn = null;
//        PreparedStatement psta = null;
//        ResultSet rs = null;
//
//        try
//        {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            conn = DriverManager.getConnection(urlSchool);
//            psta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//            int i = 0;
//            for (Object obj : Arguments)
//            {
//                i++;
//
//                if (obj != null && obj.getClass() == Date.class) // when the argument is a date then convert to a timestamp
//                {
//                    psta.setTimestamp(i, DateToTimestamp((Date) obj));
//                } else if (obj != null && obj.getClass() == String.class) // when the argument is a string then escap all html4 stuff
//                {
//                    psta.setObject(i, escapeHtml4((String) obj));
//                } else
//                {
//                    psta.setObject(i, obj);
//                }
//            }
//
//            psta.executeUpdate();
//            rs = psta.getGeneratedKeys();
//            if (rs != null)
//            {
//                if (rs.next())
//                {
//                    if (rs.getInt(1) == 0)
//                    { //maybe errors
//
//                        return -1;
//                    }
//                    return rs.getInt(1);
//                }
//            }
//            return -1;
//        } catch (SQLException e)
//        {
//            System.out.println("SQL Error " + e.getMessage());
//            System.out.println("SQL Error " + e.getErrorCode());
//            return -1;
//        } catch (ClassNotFoundException e)
//        {
//            System.out.println("Class Error " + e.getMessage());
//            return -1;
//        } finally
//        {
//            if (conn != null)
//            {
//                //close and commit
//                System.out.println("commit " + sql);
//                conn.commit();
//                conn.close();
//            }
//
//            if (psta != null)
//            {
//                psta.close();
//            }
//
//            if (rs != null)
//            {
//                rs.close();
//            }
//        }
//    }
//
//    /**
//     * reusable database function
//     *
//     * @param sql
//     * @param returnfunction
//     * @return
//     * @throws SQLException
//     */
//    private <T> ArrayList<T> GetDatabase(String sql, DatabaseReturn<T> returnfunction, Object... Arguments) throws SQLException
//    {
//        ArrayList<T> objList = new ArrayList<T>();
//
//        Connection conn = null;
//        Statement sta = null;
//        PreparedStatement psta = null;
//        ResultSet rs = null;
//
//        try
//        {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            conn = (Connection) DriverManager.getConnection(urlSchool);
//            psta = conn.prepareStatement(sql);
//
//            int i = 0;
//            for (Object obj : Arguments)
//            {
//                i++;
//
//                if (obj != null && obj.getClass() == Date.class) // when the argument is a date then convert to a timestamp
//                {
//                    psta.setTimestamp(i, DateToTimestamp((Date) obj));
//                } else if (obj != null && obj.getClass() == String.class) // when the argument is a string then escap all html4 stuff
//                {
//                    psta.setObject(i, escapeHtml4((String) obj));
//                } else
//                {
//                    psta.setObject(i, obj);
//                }
//            }
//
//            rs = psta.executeQuery();
//
//            while (rs.next())
//            {
//                objList.add(returnfunction.ReturnType(rs));
//            }
//        } catch (SQLException e)
//        {
//            System.out.println("SQL Error " + e.getMessage());
//        } catch (ClassNotFoundException e)
//        {
//            System.out.println("Class Error " + e.getMessage());
//        } finally
//        {
//            if (conn != null)
//            {
//                conn.close();
//            }
//
//            if (sta != null)
//            {
//                sta.close();
//            }
//
//            if (rs != null)
//            {
//                rs.close();
//            }
//        }
//
//        return objList;
//    }
//
//    private boolean DatabaseConnection(String connection) throws SQLException
//    {
//        Connection conn = null;
//        Statement sta = null;
//        ResultSet rs = null;
//
//        try
//        {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            conn = DriverManager.getConnection(connection);
//            sta = conn.createStatement();
//            if (conn == null)
//            {
//                return false;
//            }
//            return !conn.isClosed();
//        } catch (SQLException e)
//        {
//            System.out.println("SQL Error " + e.getMessage());
//            return false;
//        } catch (ClassNotFoundException e)
//        {
//            System.out.println("Class Error " + e.getMessage());
//            return false;
//        } finally
//        {
//            if (conn != null)
//            {
//                conn.close();
//            }
//
//            if (sta != null)
//            {
//                sta.close();
//            }
//
//            if (rs != null)
//            {
//                rs.close();
//            }
//        }
//    }
//
//    // </editor-fold>
//}
//
////    public boolean Connect() throws ClassNotFoundException
////    {
////
////        Class.forName("oracle.jdbc.driver.OracleDriver");
////
////        boolean success = false;
////
////        try
////        {
////            myConn = DriverManager.getConnection("jdbc:oracle:thin:@//fhictora01.fhict.local:1521/fhictora", "dbi310866", "O4g03ym3r8");
////        }
////        catch(SQLException ex)
////        {
////            System.out.println("Connection failed");
////        }
////
////        if(myConn != null)
////        {
////            success = true;
////        }
////        return success;
////    }
////    // query =  select * from \"User\"
////    public ResultSet GetQuery(String query)
////    {
////        try
////        {
////            myStmt = myConn.createStatement();
////            return myRs = myStmt.executeQuery(query);
////        }
////        catch(SQLException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        catch(Exception ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        return null;
////    }
////
////    // query =  select * from \"User\"
////    public ResultSet GetQuery(PreparedStatement query)
////    {
////        try
////        {
////            return query.executeQuery();
////        }
////        catch(SQLException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        catch(Exception ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        return null;
////    }
////
////    public boolean InsertQuery(String query)
////    {
////        try
////        {
////            myStmt = myConn.createStatement();
////            myStmt.executeUpdate(query);
////
////            return true;
////        }
////        catch (SQLException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        catch (Exception ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        return false;
////    }
////
////    public boolean deleteQuery(String query)
////    {
////        boolean success = false;
////        try
////        {
////            myStmt = myConn.createStatement();
////            myStmt.executeUpdate(query);
////
////            success = true;
////        }
////        catch(Exception ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////
////        return success;
////    }
////
////    public boolean InsertQuery(PreparedStatement query) throws SQLException
////    {
////        try
////        {
////            query.executeUpdate();
////
////            return true;
////        }
////        catch (SQLException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        catch (Exception ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        return false;
////    }
////
////    public void Close()
////    {
////        try
////        {
////            if (myRs != null)
////            {
////                myRs.close();
////            }
////            if (myStmt != null)
////            {
////                myStmt.close();
////            }
////            if (myConn != null)
////            {
////                myConn.close();
////            }
////        }
////        catch(SQLException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////    }
////}
//
