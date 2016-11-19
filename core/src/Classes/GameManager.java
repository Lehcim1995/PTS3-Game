package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.IRemotePublisherForListener;

import java.beans.PropertyChangeEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Nick on 11-10-2016.
 */
public class GameManager extends UnicastRemoteObject implements IGameManager, IRemotePropertyListener
{
    private static GameManager instance;
    private String name;
    private Level level;
    private ArrayList<Spectator> spectators;
    private ArrayList<Projectile> bullets;
    private ArrayList<KillLog> killLogs;
    private ArrayList<Player> playerList;
    private ArrayList<Chat> chats;
    private ArrayList<IGameObject> objects;
    private boolean gen = false;

    private IRemotePublisherForListener remotePublisherForListener;
    private IRemotePublisherForDomain remotePublisherForDomain;
    private Registry registry;
    private Timer GameTicks;
    private TimerTask GameTickTask;

    private Player playerMe;

    private GameManager() throws RemoteException
    {
        playerList = new ArrayList<Player>();
        spectators = new ArrayList<Spectator>();
        bullets = new ArrayList<Projectile>();
        killLogs = new ArrayList<KillLog>();
        chats = new ArrayList<Chat>();
        objects = new ArrayList<IGameObject>();

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
            registry = LocateRegistry.getRegistry(ip, portNumber);

            remotePublisherForListener = (IRemotePublisherForListener) registry.lookup(testBindingName);
            remotePublisherForDomain = (IRemotePublisherForDomain) registry.lookup(testBindingName);


            remotePublisherForListener.subscribeRemoteListener(this, ServerNewPlayer);
            remotePublisherForListener.subscribeRemoteListener(this, UpdatePlayer);
        }
        catch (RemoteException ex)
        {
            System.out.println("Client: RemoteExeption " + ex.getMessage());
        }
        catch (NotBoundException e)
        {
            System.out.println("Client: NotBoundException " + e.getMessage());
        }

        SpawnPlayer();

        GameTicks = new Timer();
        GameTickTask = new TimerTask()
        {
            @Override
            public void run()
            {
                //System.out.println("GameTick");
                try
                {
                    if (playerMe != null)
                    {
                        remotePublisherForDomain.inform(propertyName, null, playerMe);
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        };
        GameTicks.scheduleAtFixedRate(GameTickTask, 0, (int) TICKLENGTH/2);
    }

    public static GameManager getInstance()
    {
        try
        {
            return instance == null ? (instance = new GameManager()) : instance;
        }
        catch (RemoteException ex)
        {
            System.out.println("Client Remote error " + ex.getMessage());
        }
        return instance;
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
        for (IGameObject go1 : objects)
        {
            for (IGameObject go2 : objects)
            {
                if (go1 != go2)
                {
                    if (go1.isHit(go2))
                    {
                        //System.out.println(go1.getClass().toString());
                        //go1.OnCollisionEnter(go2);
                        hitlist.add(new GameObject[]{(GameObject) go1, (GameObject) go2});
                    }
                }
            }
        }

        for (GameObject[] golist : hitlist)
        {
            golist[0].OnCollisionEnter(golist[1]);
        }
    }

    public void SpawnPlayer()
    {
        System.out.println("Client New Player");
//        try
//        {
//            playerMe = new Player();
//            //addPlayer(playerMe);
//        }
//        catch (RemoteException e)
//        {
//            System.out.println("Remote Exception " + e.getMessage());
//        }

        try
        {
            remotePublisherForDomain.inform(ClientNewPlayer, null, playerMe);
        }
        catch (RemoteException e)
        {
            System.out.println("Remote Exception " + e.getMessage());
        }
    }

    public Player GetPlayer()
    {
        return playerMe;
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

    public Player GetSpectatedPlayer(int item)
    {

        while (item < 0)
        {
            item += playerList.size();
        }

        return playerList.get(item % playerList.size());
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
        //System.out.println("Client new " + go.getClass().getName());
    }

    public void addPlayer(Player pl)
    {
        playerList.add(pl);
        addGameObject(pl);
    }

    public ArrayList<IGameObject> getObjects()
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
    public synchronized void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException
    {
        if (propertyChangeEvent.getPropertyName().equals(UpdatePlayer))
        {
            //Vector2 pos = (Vector2) propertyChangeEvent.getNewValue();
            ArrayList<IGameObject> list = (ArrayList<IGameObject>) propertyChangeEvent.getNewValue();
            for (IGameObject go : list)
            {
                Player p = (Player)go;
                for (Player p2 : playerList)
                {
                    if (p.GetName().equals(p2.GetName()))
                    {
                        p2.SetPosition(p.GetPosition());
                        break;
                    }
                }
            }

            //playerMe.SetPosition(pos);
        }

        if (propertyChangeEvent.getPropertyName().equals(ServerNewPlayer))
        {
            System.out.println("Client New Player");

            ArrayList<IGameObject> list = (ArrayList<IGameObject>) propertyChangeEvent.getNewValue();
            Collections.reverse(list);
            for (IGameObject go : list)
            {

                Player p = (Player) go;
                Player p2 = null;

                if (playerMe == null)
                {
                    p2 = new Player(p, true);
                    System.out.println("Client New Playable Player " + p2.GetName());
                    playerMe = p2;
                }
                else
                {
                    if (p.GetName().equals(playerMe.GetName()))
                    {

                    }
                    else
                    {
                        p2 = new Player(p, false);
                    }
                }

                if (p2 != null)
                {
                    addPlayer(p2);
                }
            }
        }

        //List<GameObject> objectsList = (List<GameObject>) propertyChangeEvent.getNewValue();
        //objects = (ArrayList<GameObject>) objectsList;
    }
}
