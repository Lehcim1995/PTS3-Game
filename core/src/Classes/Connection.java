package Classes;

import Interfaces.IConnection;
import Interfaces.IUser;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Connection extends UnicastRemoteObject implements IConnection
{
    private transient Database database;

    public Connection() throws RemoteException
    {
            database = Database.InstanceGet();
    }
    @Override
    public boolean CreateUser(String name, String lastname, String email, String username, String password)
    {
        if (!name.equals("") && !lastname.equals("") && email.contains("@") && !username.equals("") && !password.equals("")) {
//        String query = "INSERT INTO USER_TABLE(\"ID\", \"NAME\", \"LASTNAME\", \"EMAIL\", \"USERNAME\", \"PASSWORD\") VALUES (USERPK_SEQ.nextval, " + name + ", " + lastname + ", "  + email + ", " + username + ", " + password + ")";
            String query = "INSERT INTO USER_TABLE([ID], [NAME], [LASTNAME], [EMAIL], [USERNAME], [PASSWORD]) VALUES (?, ?, ?, ?, ?, ?)";
            List<Object> arguments = new ArrayList();
            arguments.add("SELECT NEXT VALUE FOR USER_SEQ;");
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
    @Override
    public IUser LogIn(String email, String password)
    {
        if(email.contains("@") && !password.equals(""))
        {
            String LogInQuery = "SELECT * FROM USER_Table WHERE EMAIL = '" + email + "' AND PASSWoRD = '" + password + "'";
            //String LogInQuery = "SELECT * FROM [USER_TABLE] WHERE EMAIL = ? AND [PASSWORD] = ?";
            List<Object> arguments = new ArrayList();
            arguments.add(email);
            arguments.add(password);

            try
            {
                //success = true;
                System.out.println(LogInQuery);
                //ArrayList<User> resultSet = database.LogIn(LogInQuery, arguments);
                ///TODO: Connectionklasse fixen en zorgen dat er juiste gegevens worden opgehaald
                ///TODO: Mogelijk met Prepared statement (Pas connectionklasse aan naar MYSQL implementation
                ///TODO: geen gebruik van LogInNoArgs() maar Login()!
                ArrayList<User> resultSet = database.LogInNoArgs(LogInQuery);
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
                System.out.println(ex.getMessage());
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
