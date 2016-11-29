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
import java.util.concurrent.CopyOnWriteArrayList;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

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

    private List<Player> playerList;
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

        playerList = new CopyOnWriteArrayList<Player>();
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
            LOGGER.info("Client: UnknownHostException: " + e.getMessage());
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
            LOGGER.info("Client: RemoteExeption " + ex.getMessage());
        }
        catch (NotBoundException e)
        {
            LOGGER.info("Client: NotBoundException " + e.getMessage());
        }

        SpawnPlayer();

        GameTicks = new Timer();
        GameTickTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    if (playerMe != null)
                    {
                        remotePublisherForDomain.inform(GetPlayer, null, playerMe);
                        //TODO dont use GetPlayer but use the playername
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        };
        GameTicks.scheduleAtFixedRate(GameTickTask, 0, (int) TICKLENGTH);
    }

    public static GameManager getInstance()
    {
        try
        {
            return instance == null ? (instance = new GameManager()) : instance;
        }
        catch (RemoteException ex)
        {
            LOGGER.info("Client Remote error " + ex.getMessage());
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
        try
        {
            //User u = new User();
            //TODO get name from User
            //Random r = new Random();
            String name = "Speler ";

            remotePublisherForDomain.inform(ClientNewPlayer, null, name);
        }
        catch (RemoteException e)
        {
            LOGGER.info("Remote Exception " + e.getMessage());
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

    public synchronized void addPlayer(Player pl)
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

            //TODO don't use this
            //Vector2 pos = (Vector2) propertyChangeEvent.getNewValue();
            ArrayList<IGameObject> list = (ArrayList<IGameObject>) propertyChangeEvent.getNewValue();


            for (IGameObject go : list)
            {
                if (go.getClass() == Player.class)
                {
                    Player p = (Player)go;
                    for (Player p2 : playerList)
                    {
                        if (p.GetName().equals(p2.GetName()))
                        {
                            p2.SetPosition(p.GetPosition());
                            p2.SetRotation(p.GetRotation());
                            break;
                        }
                    }
                }
            }


            //playerMe.SetPosition(pos);
        }

        if (propertyChangeEvent.getPropertyName().equals(ServerNewPlayer))
        {
            try
            {


                Player p = (Player) propertyChangeEvent.getNewValue();
                Map<String, IGameObject> players = (Map<String, IGameObject>) propertyChangeEvent.getOldValue();

                if (playerMe == null)
                {
                    playerMe = new Player(p, true);
                    addPlayer(playerMe);
                }

                for (Map.Entry<String, IGameObject> set : players.entrySet())
                {
                    System.out.println(set.getKey());
                    for (Player pl : playerList)
                    {
                        if (!set.getKey().equals(pl.GetName()))
                        {
                            Player newPlayer = new Player((Player) set.getValue(), false);
                            addPlayer(newPlayer);
                        }
                    }
                }
            }
            catch (ConcurrentModificationException cmex)
            {
                LOGGER.log(java.util.logging.Level.WARNING, cmex.getMessage(), cmex);
            }
        }
    }
}
