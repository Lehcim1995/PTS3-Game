package Classes;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Connection
{
    private Database database;
    public Connection() throws SQLException
    {
        database = Database.InstanceGet();
    }

    public boolean CreateUser(String name, String lastname, String email, String username, String password)
    {
        if (!name.equals("") && !lastname.equals("") && email.contains("@") && !username.equals("") && !password.equals("")) {
//        String query = "INSERT INTO USER_TABLE(\"ID\", \"NAME\", \"LASTNAME\", \"EMAIL\", \"USERNAME\", \"PASSWORD\") VALUES (USERPK_SEQ.nextval, " + name + ", " + lastname + ", "  + email + ", " + username + ", " + password + ")";
            String query = "INSERT INTO USER_TABLE([ID], [NAME], [LASTNAME], [EMAIL], [USERNAME], [PASSWORD]) VALUES (?, ?, ?, ?, ?, ?)";
            List<Object> arguments = new ArrayList();
            arguments.add("USERPK_SEQ.nextval");
            arguments.add(name);
            arguments.add(lastname);
            arguments.add(email);
            arguments.add(username);
            arguments.add(password);


            database.SetDatabase(query, arguments);
            return true;
        }
        return false;
    }

    public User LogIn(String email, String password) throws SQLException, Exception
    {
        if(email.contains("@") && !password.equals(""))
        {
            //String LogInQuery = "SELECT * FROM Company WHERE username = '" + username + "' AND password = '" + password + "'";
            String LogInQuery = "SELECT * FROM [USER_TABLE] WHERE EMAIL = ? AND [PASSWORD] = ?";
            List<Object> arguments = new ArrayList();
            arguments.add(email);
            arguments.add(password);

            try
            {
                //success = true;
                ArrayList<User> resultSet = database.LogIn(LogInQuery, arguments);
//                if(resultSet != null)
//                {
                    //success = true;
//                            if(resultSet.next())
//                            {
//                                return new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getInt("kills"), resultSet.getInt("deaths"), resultSet.getInt("shotshit"), resultSet.getInt("shots"), resultSet.getInt("matchesplayed"), resultSet.getInt("matcheswon"), resultSet.getInt("matcheslost"), resultSet.getInt("isbanned"));
//                            }
                    if(resultSet.size() == 1)
                    {
                        return resultSet.get(0);
                    }
//                }
            }
            catch(Exception ex)
            {
                LOGGER.info(ex.getMessage());
            }
        }

        return null;
    }

    public void UpdateStats(User user) {

//        String query = "UPDATE \"USER_TABLE\" SET kills = ?, deaths = ?, shotshit = " + user.GetShotsHit() + ", shots = " + user.GetShots()
//                + ", matchesplayed = " + user.GetMatchesPlayed() + ", matcheswon = " + user.GetMatchesWon() + ", matcheslost = " + user.GetMatchesLost() + " WHERE email = " + user.GetEmail();
        String query = "UPDATE [USER_TABLE] SET [kills] = ?, [deaths] = ?, [shotshit] = ?, [shots] = ?, [matchesplayed] = ?, [matcheswon] = ?, [matcheslost] = ? WHERE [email] = ?";
        List<Object> arguments = new ArrayList();
        arguments.add(user.GetKills());
        arguments.add(user.GetDeaths());
        arguments.add(user.GetShotsHit());
        arguments.add(user.GetShots());
        arguments.add(user.GetMatchesPlayed());
        arguments.add(user.GetMatchesWon());
        arguments.add(user.GetMatchesLost());
        arguments.add(user.GetEmail());

        database.SetDatabase(query, arguments);
    }

    public void BanUser(User user) {

        String query = "UPDATE [USER_TABLE] SET [isbanned] = 1 WHERE [email] = ?";
        List<Object> arguments = new ArrayList();
        arguments.add(user.GetEmail());

        database.SetDatabase(query, arguments);
    }
}
