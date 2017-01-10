package Classes;

import java.rmi.RemoteException;
import java.util.*;
/**
 * Created by Jasper on 11-10-2016.
 */
public class Spectator extends GameObject
{
    private String name;
    private GameManager gm;
    private int playerFromList;
    private Player player;

    public Spectator(String name) throws RemoteException
    {
        this.name = name;
        //this.gm = gameManager;
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
        List<Player> playerList = GameManager.getInstance().GetSpectatedPlayer();
        if(!playerList.isEmpty())
        {
            if(playerFromList < 0)
                playerFromList =  playerList.size()-1;
            if(playerFromList > playerList.size() -1)
                playerFromList = 0;
            //TODO: if no players in game to view error
            player = playerList.get(playerFromList);
            GameManager.getInstance().setSpectator(this);
        }
    }

    public void NextPlayer() {
        playerFromList++;
        setSpectatedPlayer();
    }

    public void PrevPlayer() {
        playerFromList--;
        setSpectatedPlayer();
    }

    public Player getSpectatedPlayer(){
        return player;
    }
}
