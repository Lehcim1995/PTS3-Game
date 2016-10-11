package Classes;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;

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
        playerList = new ArrayList<Player>();
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
        /*Vector2 playerPosition;
        Vector2 projectilePosition = projectile.GetPosition();
        for (Player p: playerList)
        {
            playerPosition = p.GetPosition();

        }*/
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
        playerList.remove(player);
    }

    public void LeaveMatch(Spectator spectator)
    {
        spectators.remove(spectator);
    }

    public void Chat(String message, Player player)
    {
        Chat chat = new Chat(message, player);
        chats.add(chat);
    }

    public Player GetSpectatedPlayer(int item) {

        while (item < 0)
        {
            item += playerList.size();
        }

        return  playerList.get(item % playerList.size());
    }

    public void AddProjectile(Projectile projectile)
    {
        bullets.add(projectile);
    }
}
