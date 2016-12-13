package Classes;

import Interfaces.IConnection;
import Interfaces.IUser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Jasper on 19-11-2016.
 */
public class Connection extends UnicastRemoteObject implements IConnection
{
    private transient Database database;

    public Connection() throws RemoteException
    {
        database = Database.getInstance();
    }

    @Override
    public boolean CreateUser(String name, String lastname, String email, String username, String password)
    {

        if (!name.isEmpty() && !lastname.isEmpty() && email.contains("@") && !username.isEmpty() && !password.isEmpty())
        {
            String query = "INSERT INTO USER_TABLE(NAME, LASTNAME, EMAIL, USERNAME, PASSWORD) VALUES (?,?,?,?,?)";

            database.setDatabase(query, name, lastname, email, username, password);
            return true;
        }
        return false;
    }

    @Override
    public IUser LogIn(String email, String password)
    {
        if (email.contains("@") && !password.isEmpty())
        {
            String LogInQuery = "SELECT * FROM USER_Table WHERE EMAIL = ? AND PASSWORD = ?";

            try
            {
                System.out.println(LogInQuery);
                ArrayList<User> resultSet = database.LogIn(LogInQuery, email, password);
                if (resultSet.size() == 1)
                {
                    return resultSet.get(0);
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        return null;
    }


    public void UpdateStats(User user)
    {
        String query = "UPDATE USER_TABLE SET kills = ?, deaths = ?, shotshit = ?, shots = ?, matchesplayed = ?, matcheswon = ?, matcheslost = ? WHERE email = ?";

        database.setDatabase(query, user.getKills(), user.getDeaths(), user.getShotsHit(), user.getShots(), user.getMatchesPlayed(),  user.getMatchesWon(), user.getMatchesLost(), user.getEmail());
    }

    public void BanUser(User user)
    {
        String query = "UPDATE USER_TABLE SET isbanned = 1 WHERE email = ?";

        database.setDatabase(query, user.getEmail());
    }
}
