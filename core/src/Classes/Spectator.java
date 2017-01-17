package Classes;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jasper on 11-10-2016.
 */
public class Spectator extends GameObject
{
    private String name;
    //private GameManager gm;
    private int playerFromList;
    private Player player;
    private Logger logger;
    private boolean AddedUser;

    public Spectator(String name) throws RemoteException
    {
        logger = Logger.getAnonymousLogger();
        this.name = name;

        this.playerFromList = 0;



    }

    public String getName()
    {
        return name;
    }

    public String GetSpectatedName()
    {
        String spectatedName = "No player available to spectate";

        if (player != null)
        {
            spectatedName = player.getName();
        }

        return spectatedName;
    }

    @Override
    public void update() throws RemoteException {
        super.update();
        if (!AddedUser)
        {
            setSpectatedPlayer();
            AddedUser = true;
        }
    }

    public void setSpectatedPlayer()
    {
        List<Player> playerList = GameManager.getInstance().GetSpectatedPlayer();
        if (!playerList.isEmpty())
        {
            if (playerFromList < 0) playerFromList = playerList.size() - 1;
            if (playerFromList > playerList.size() - 1) playerFromList = 0;
            //TODO: if no players in game to view error
            player = playerList.get(playerFromList);
            GameManager.getInstance().setSpectator(this);
        }
        else
        {
            logger.info("er zijn nog geen spelers om naar te kijken.");
        }
    }

    public void NextPlayer()
    {
        playerFromList++;
        setSpectatedPlayer();
    }

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
