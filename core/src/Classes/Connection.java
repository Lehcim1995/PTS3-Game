package Classes;

import Interfaces.IConnection;
import Interfaces.IUser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Connection extends UnicastRemoteObject implements IConnection
{
    private transient Database database;
    /**
     * Connection Constructor
     */
    public Connection() throws RemoteException
    {
        database = Database.getInstance();
    }

    @Override
    public boolean CreateUser(String name, String lastname, String email, String username, String password)
    {

        if (!name.isEmpty() && !lastname.isEmpty() && email.contains("@"))
        {
            if(!username.isEmpty() && !password.isEmpty())
            {
                String query = "INSERT INTO USER_TABLE(NAME, LASTNAME, EMAIL, USERNAME, PASSWORD) VALUES (?,?,?,?,?)";

                database.setDatabase(query, name, lastname, email, username, password);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public IUser LogIn(String email, String password)
    {
        if (email.contains("@") && !password.isEmpty())
        {
            String logInQuery = "SELECT * FROM USER_Table WHERE EMAIL = ? AND PASSWORD = ?";

            try
            {
                System.out.println(logInQuery);
                ArrayList<User> resultSet = database.LogIn(logInQuery, email, password);
                if (resultSet.size() == 1)
                {
                    return resultSet.get(0);
                }
            }
            catch (Exception e)
            {
                LOGGER.log(Level.WARNING, e.getMessage(), e );
            }
        }

        return null;
    }
    /**
     * Update Stats of a User.
     * @parm User - User object that contains the new stats.
     */
    public void UpdateStats(User user)
    {
        String query = "UPDATE USER_TABLE SET kills = ?, deaths = ?, shotshit = ?, shots = ?, matchesplayed = ?, matcheswon = ?, matcheslost = ? WHERE email = ?";

        database.setDatabase(query, user.getKills(), user.getDeaths(), user.getShotsHit(), user.getShots(), user.getMatchesPlayed(),  user.getMatchesWon(), user.getMatchesLost(), user.getEmail());
    }
    /**
     * Ban the user by User.
     * @parm User - User object that is getting banned.
     */
    public void BanUser(User user)
    {
        String query = "UPDATE USER_TABLE SET isbanned = 1 WHERE email = ?";

        database.setDatabase(query, user.getEmail());
    }
}
