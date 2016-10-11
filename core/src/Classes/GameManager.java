package Classes;

import java.util.ArrayList;

/**
 * Created by Nick on 11-10-2016.
 */
public class GameManager
{
    private String name;
    private Level level;
    private ArrayList<Spectator> spectators;
    private ArrayList<Projectile> bullets;
    private ArrayList<KillLog> killLogs;
    private ArrayList<Player> playerList;
    private ArrayList<Chat> chats;

    public GameManager(ArrayList<Player> players, Level level, String name)
    {
        this.name = name;
        this.playerList = players;
        this.level = level;
        spectators = new ArrayList<Spectator>();
        bullets = new ArrayList<Projectile>();
        killLogs = new ArrayList<KillLog>();
        chats = new ArrayList<Chat>();
    }

    public void Update()
    {

    }

    public void CheckHit(Projectile projectile)
    {

    }

    public void SpawnPlayer(Player player)
    {

    }

    public void StartMatch()
    {

    }

    public void EndMatch()
    {

    }

    public void LeaveMatch(Player player)
    {

    }

    public void Chat(String message, Player player)
    {

    }

    public Player GetSpectatedPlayer(int item) {
        return playerList.get(item);
    }
}
