package Classes;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Jasper on 11-10-2016.
 */
public class Spectator extends GameObject
{
    private String name;
    private int playerFromList;
    private Player player;
    private Logger logger;
    /**
     * Spectator constructor
     *
     * @param name    - Name of the player that is spectator
     */
    public Spectator(String name) throws RemoteException
    {
        logger = Logger.getAnonymousLogger();
        this.name = name;

        this.playerFromList = 0;

        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                try
                {
                    Thread.sleep(100);
                    setSpectatedPlayer();
                }
                catch (InterruptedException e)
                {
                    LOGGER.log(Level.SEVERE, "Speed interruped: " + e);
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public String getName()
    {
        return name;
    }
    /**
     * Get the name of player being spectated
     *
     */
    public String getSpectatedName()
    {
        String spectatedName = "No player available to spectate";

        if (player != null)
        {
            spectatedName = player.getName();
        }

        return spectatedName;
    }
    /**
     * Set the player that is being spectated
     *
     */
    public void setSpectatedPlayer()
    {
        List<Player> playerList = GameManager.getInstance().GetSpectatedPlayer();
        if (!playerList.isEmpty())
        {
            if (playerFromList < 0)
            {
                playerFromList = playerList.size() - 1;
            }
            if (playerFromList > playerList.size() - 1)
            {
                playerFromList = 0;
            }
            //TODO: if no players in game to view error
            player = playerList.get(playerFromList);
            GameManager.getInstance().setSpectator(this);
        }
        else
        {
            LOGGER.log(Level.INFO, "er zijn nog geen spelers om naar te kijken.");
        }
    }
    /**
     * Specatate the next player
     *
     */
    public void NextPlayer()
    {
        playerFromList++;
        setSpectatedPlayer();
    }
    /**
     * Specatate the previous player
     *
     */
    public void PrevPlayer()
    {
        playerFromList--;
        setSpectatedPlayer();
    }

    public Player getSpectatedPlayer()
    {
        return player;
    }


}
