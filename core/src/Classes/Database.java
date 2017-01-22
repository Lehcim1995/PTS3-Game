/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * @author michel
 */
public class Database
{


    private static final String serverNameSchool = "localhost";
    private static final String mydatabase = "asom";
    private static final String username = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://" + serverNameSchool + ":3306/" + mydatabase + "?&relaxAutoCommit=true";
    private static Database instance;
    private DatabaseReturn<User> userReturn = (set) -> //Lambda voor het ophalen van user
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

    private Database()
    {
        try
        {
            if (DatabaseConnection(url))
            {
                Logger.getAnonymousLogger().log(Level.INFO, "School db connected");
            }
            else
            {
                Logger.getAnonymousLogger().log(Level.INFO, "Cannot connect to any database");
            }
        }
        catch (SQLException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "SQLError in Constructor: " + e.getMessage(), e);
        }
    }

    /**
     * @return The singleton of the database
     */
    public static Database getInstance()
    {
        return instance == null ? instance = new Database() : instance; // kan helaas niet kleiner :'(
    }

    //<editor-fold defaultstate="collapsed" desc=" Raw DbFunctions ">

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

    /**
     * Makes a save SQL statement and executes it
     *
     * @param sql       The query, use an "?" at the place of a input. Like this:
     *                  INSERT INTO TABLE('name', 'lastname' , enz ) VALUES(?,?, enz);
     * @param arguments The arguments correspont to same questionmark.
     * @return The generated key
     * @throws SQLException
     */
    public Integer setDatabase(String sql, Object... arguments)
    {
        Connection conn = null;
        PreparedStatement psta = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            psta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            EscapeSQL(psta, arguments);

            psta.executeUpdate();
            rs = psta.getGeneratedKeys();
            if (rs != null && rs.next())
            {
                if (rs.getInt(1) == 0)
                { //maybe errors
                    return -1;
                }
                return rs.getInt(1);
            }
            return -1;
        }
        catch (SQLException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "SQL Error: " + e.getMessage(), e);
            return -1;
        }
        catch (ClassNotFoundException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "Class Error " + e.getMessage(), e);
            return -1;
        }
        finally
        {
            if (conn != null)
            {
                //close and commit
                Logger.getAnonymousLogger().log(Level.INFO, "Commit" + sql);
                try
                {
                    conn.commit();
                }
                catch (SQLException e)
                {
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
                }
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
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
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
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
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
    }

    public Integer setDatabaseNoArgs(String sql)
    {
        return setDatabase(sql);
    }

    private void EscapeSQL(PreparedStatement preparedStatement, Object... arguments) throws SQLException
    {
        int i = 0;
        for (Object obj : arguments)
        {
            i++;

            if (obj != null && obj.getClass() == Date.class) // when the argument is a date then convert to a timestamp
            {
                preparedStatement.setTimestamp(i, DateToTimestamp((Date) obj));
            }
            else if (obj != null && obj.getClass() == String.class) // when the argument is a string then escap all html4 stuff
            {
                preparedStatement.setObject(i, escapeHtml4((String) obj));
            }
            else
            {
                preparedStatement.setObject(i, obj);
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
    private <T> ArrayList<T> getDatabase(String sql, DatabaseReturn<T> returnfunction, Object... arguments) throws SQLException
    {
        ArrayList<T> objList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement psta = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            psta = conn.prepareStatement(sql);

            EscapeSQL(psta, arguments);

            rs = psta.executeQuery();

            while (rs.next())
            {
                objList.add(returnfunction.ReturnType(rs));
            }
        }
        catch (SQLException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "SQL Error: " + e.getMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "Class Error: " + e.getMessage(), e);
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
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
        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(connection, username, password);
            sta = conn.createStatement();
            if (conn == null)
            {
                return false;
            }
            return !conn.isClosed();
        }
        catch (SQLException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "SQL Error: " + e.getMessage(), e);
            return false;
        }
        catch (ClassNotFoundException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, "Class Error: " + e.getMessage(), e);
            return false;
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }

            if (sta != null)
            {
                sta.close();
            }
        }
    }

    /**
     * Login check in database.
     *
     * @param query     The querry used
     * @param arguments Login Data
     */
    public ArrayList<User> LogIn(String query, Object... arguments)
    {
        try
        {
            return getDatabase(query, userReturn, arguments);
        }
        catch (SQLException e)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * inteface to use as a delegete
     *
     * @param <T> The generic of the interface
     */
    interface DatabaseReturn<T>
    {

        /**
         * @param set the resultset from the query
         * @return returns whatever you want
         * @throws SQLException
         */
        T ReturnType(ResultSet set) throws SQLException;
    }
}
