package Classes;

import java.util.ArrayList;

/**
 * Created by Nick on 11-10-2016.
 */
public class GameManager
{
    private static GameManager instance;
    private String name;
    private Level level;
    private ArrayList<Spectator> spectators;
    private ArrayList<Projectile> bullets;
    private ArrayList<KillLog> killLogs;
    private ArrayList<Player> playerList;
    private ArrayList<Chat> chats;

    private GameManager()
    {
        spectators = new ArrayList<Spectator>();
        bullets = new ArrayList<Projectile>();
        killLogs = new ArrayList<KillLog>();
        chats = new ArrayList<Chat>();
    }

    public static GameManager getInstance()
    {
        return instance == null ? instance = new GameManager() : instance;
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
        Chat chat = new Chat(message, player);
        chats.add(chat);
    }

    public Player GetSpectatedPlayer(int item) {
        return playerList.get(item);
    }
}
