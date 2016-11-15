package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import com.badlogic.gdx.graphics.Color;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.IRemotePublisherForListener;
import fontyspublisher.RemotePublisher;
import sun.security.x509.IPAddressName;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * Created by Nick on 11-10-2016.
 */
public class GameManager implements IGameManager, IRemotePropertyListener
{
    private static GameManager instance;
    private String name;
    private Level level;
    private ArrayList<Spectator> spectators;
    private ArrayList<Projectile> bullets;
    private ArrayList<KillLog> killLogs;
    private ArrayList<Player> playerList;
    private ArrayList<Chat> chats;
    private ArrayList<GameObject> objects;
    private boolean gen = false;

    private IRemotePublisherForListener remotePublisherForListener;
    private Registry registry;
    private Timer GameTicks;
    private TimerTask GameTickTask;

    private GameManager()
    {
        playerList = new ArrayList<Player>();
        spectators = new ArrayList<Spectator>();
        bullets = new ArrayList<Projectile>();
        killLogs = new ArrayList<KillLog>();
        chats = new ArrayList<Chat>();
        objects =  new ArrayList<GameObject>();

        InetAddress localhost = null;
        try
        {
            localhost = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Client: UnknownHostException: " + e.getMessage());
        }
        String ip = localhost.getHostAddress();


        try
        {
            registry = LocateRegistry.getRegistry(ip,portNumber);

            remotePublisherForListener = (IRemotePublisherForListener) registry.lookup(bindingName);
            remotePublisherForListener.subscribeRemoteListener(this, propertyName);
        }
        catch (RemoteException ex)
        {
            System.out.println("Client: RemoteExeption " + ex.getMessage());
        }
        catch (NotBoundException e)
        {
            System.out.println("Client: NotBoundException " + e.getMessage());
        }

        GameTicks = new Timer();
        GameTickTask = new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println(playerList.size());
            }
        };
        GameTicks.scheduleAtFixedRate(GameTickTask, 0, 2000);
    }

    public static GameManager getInstance()
    {
        return instance == null ? (instance = new GameManager()) : instance;
    }

    public void Update() throws RemoteException
    {
        if (!gen)
        {
            StartMatch();
        }

        ArrayList<GameObject> g = (ArrayList<GameObject>) objects.clone();
        Iterator iterator = g.iterator();
        while (iterator.hasNext())
        {
            GameObject go1 = (GameObject) iterator.next();
            go1.Update();
        }

        ArrayList<GameObject[]> hitlist = new ArrayList<GameObject[]>();
        for (GameObject go1: objects)
        {
            for (GameObject go2: objects)
            {
                if (go1 != go2)
                {
                    if (go1.isHit(go2))
                    {
                        //System.out.println(go1.getClass().toString());
                        //go1.OnCollisionEnter(go2);
                        hitlist.add(new GameObject[]{go1, go2});
                    }
                }
            }
        }

        for (GameObject[] golist : hitlist)
        {
            golist[0].OnCollisionEnter(golist[1]);
        }
    }

    public void SpawnPlayer(Player player)
    {
        player.Spawn();
    }

    //TODO start a match
    public void StartMatch() throws RemoteException
    {
        level = new Level();
        gen = true;
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
        addGameObject(projectile);
    }

    public void ClearProjectiles()
    {
        objects.removeAll(bullets);
        bullets.clear();
    }

    public void ClearProjectile(Projectile p)
    {
        objects.remove(p);
        bullets.remove(p);
    }

    public ArrayList<Projectile> GetProjectile()
    {
        return bullets;
    }

    public void addGameObject(GameObject go)
    {
        objects.add(go);
        System.out.println("Client new " + go.getClass().getName());
    }

    public void addPlayer(Player pl)
    {
        playerList.add(pl);
        addGameObject(pl);
    }

    public ArrayList<GameObject> getObjects()
    {
        return objects;
    }

    @Override
    public List<IGameObject> GetTick()
    {
        return null;
    }

    @Override
    public void SetTick(IGameObject object)
    {

    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException
    {
        //System.out.println("List lenght " + ((ArrayList<IGameObject>)propertyChangeEvent.getNewValue()).size());

        Color color = (Color)propertyChangeEvent.getNewValue();

        if (playerList != null && playerList.size() > 0)
        {
            playerList.get(0).SetColor(color);
        }

//        for(IGameObject go : (ArrayList<IGameObject>)propertyChangeEvent.getNewValue())
//        {
//            addGameObject((GameObject) go);
//        }
    }
}
