package Classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Connection
{
    private Database database;
    public Connection() throws SQLException
    {
        database = new Database();
    }

    public boolean CreateUser(String name, String lastname, String email, String username, String password)
    {
        String query = "INSERT INTO USER_TABLE(\"ID\", \"NAME\", \"LASTNAME\", \"EMAIL\", \"USERNAME\", \"PASSWORD\") VALUES (USERPK_SEQ.nextval, " + name + ", " + lastname + ", "  + email + ", " + username + ", " + password + ")";

        try
        {
            if(database.Connect())
            {
                database.InsertQuery(query);
            }
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.info(e.getMessage());

            return false;
        }
        return true;
    }

    public User LogIn(String email, String password) throws SQLException, Exception
    {
        if(email != null && password != null)
        {
            //String LogInQuery = "SELECT * FROM Company WHERE username = '" + username + "' AND password = '" + password + "'";
            String LogInQuery = "SELECT * FROM \"USER_TABLE\" WHERE EMAIL = '" + email + "' AND \"PASSWORD\" = '" + password + "'";
            try
            {
                //success = true;
                if(database.Connect())
                {
                    try
                    {
                        //success = true;
                        ResultSet resultSet = database.GetQuery(LogInQuery);
                        if(resultSet != null)
                        {
                            //success = true;
                            if(resultSet.next())
                            {
                                return new User(resultSet.getString("name") + " " + resultSet.getString("lastname"), resultSet.getString("email"), resultSet.getInt("kills"), resultSet.getInt("deaths"), resultSet.getInt("shotshit"), resultSet.getInt("shots"), resultSet.getInt("matchesplayed"), resultSet.getInt("matcheswon"), resultSet.getInt("matcheslost"), resultSet.getInt("isbanned"));
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        LOGGER.info(ex.getMessage());
                    }
                    finally
                    {
                        database.Close();
                    }
                }
            }
            catch(Exception ex)
            {
                LOGGER.info(ex.getMessage());
            }
        }

        return null;
    }

    public void UpdateStats(User user) {

        String query = "UPDATE \"USER_TABLE\" SET kills = " + user.GetKills() + ", deaths = " + user.GetDeaths() + ", shotshit = " + user.GetShotsHit() + ", shots = " + user.GetShots()
                + ", matchesplayed = " + user.GetMatchesPlayed() + ", matcheswon = " + user.GetMatchesWon() + ", matcheslost = " + user.GetMatchesLost() + " WHERE email = " + user.GetEmail();

        try
        {
            if(database.Connect())
            {
                database.InsertQuery(query);
            }
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.info(e.getMessage());
        }
    }

    public void BanUser(User user) {

        String query = "UPDATE \"USER_TABLE\" SET isbanned = 1 WHERE email = " + user.GetEmail();

        try
        {
            if(database.Connect())
            {
                database.InsertQuery(query);
            }
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.info(e.getMessage());
        }
    }
}
