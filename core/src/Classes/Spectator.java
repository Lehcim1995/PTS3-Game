package Classes;

import com.badlogic.gdx.Gdx;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jasper on 11-10-2016.
 */
public class Spectator extends GameObject
{
    private String name;
    private int playerFromList;
    private Player player;
    private transient Logger logger;
    private boolean addedUser;
    private transient InputClass inputClass;
    /**
     * Spectator Constructor
     *
     * @param name - Name of the player that is spectator
     */
    public Spectator(String name) throws RemoteException
    {
        logger = Logger.getAnonymousLogger();
        this.name = name;

        this.playerFromList = 0;
        inputClass = new InputClass(this);
        Gdx.input.setInputProcessor(inputClass);
    }

    public String getName()
    {
        return name;
    }
    /**
     * Gets spectators name
     *
     */
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
        if (!addedUser)
        {
            setSpectatedPlayer();
            addedUser = true;
        }
    }
    /**
     * Set a player to be spectated by the specatator
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
            player = playerList.get(playerFromList);
            GameManager.getInstance().setSpectator(this);
        }
        else
        {
            logger.info("er zijn nog geen spelers om naar te kijken.");
        }
    }
    /**
     * Next player in the list
     *
     */
    public void NextPlayer()
    {
        playerFromList++;
        setSpectatedPlayer();
    }
    /**
     * Previous player in the list
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
