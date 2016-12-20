package Classes;

import java.rmi.RemoteException;

/**
 * Created by Jasper on 11-10-2016.
 */
public class Spectator extends GameObject
{
    private String name;
    private GameManager gm;
    private int playerFromList;
    private Player player;

    public Spectator(String name, GameManager gameManager) throws RemoteException
    {
        this.name = name;
        this.gm = gameManager;
        this.playerFromList = 0;
        setSpectatedPlayer();
    }

    public String getName() {
        return name;
    }

    public String GetSpectatedName() {
        String spectatedName = "No player available to spectate";

        if (player != null) {
            spectatedName = player.getName();
        }

        return spectatedName;
    }

    public void setSpectatedPlayer() {
        Player p = gm.GetSpectatedPlayer(playerFromList);
        if(p == null)
        {
            playerFromList = 0;
            setSpectatedPlayer();
        }
        else
            player = p;
    }

    public void NextPlayer() {
        playerFromList++;
        setSpectatedPlayer();
    }

    public void PrevPlayer() {
        playerFromList--;
        setSpectatedPlayer();
    }
}
