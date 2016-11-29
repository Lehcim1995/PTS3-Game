package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializableColor;
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
public class GameManager extends UnicastRemoteObject implements IGameManager
{
    private static GameManager instance;
    private final Level level;
    private ArrayList<KillLog> killLogs;
    private String name;

    private List<IGameObject> objects;
    private List<IGameObject> notMine;
    private boolean gen = false;
    private boolean online = false;

    private IGameManager Server;
    private Registry registry;
    private Timer GameTicks;
    private TimerTask GameTickTask;

    private Player playerMe;

    private GameManager() throws RemoteException
    {

        //spectators = new ArrayList<Spectator>();
        killLogs = new ArrayList<KillLog>();
        //chats = new ArrayList<Chat>();
        objects = new ArrayList<IGameObject>();
        notMine = new ArrayList<IGameObject>();

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

            Server = (IGameManager) registry.lookup(ServerManger);
        }
        catch (RemoteException ex)
        {
            LOGGER.info("Client: RemoteExeption " + ex.getMessage());
        }
        catch (NotBoundException e)
        {
            e.printStackTrace();
        }
        Random r =new Random();
        name = r.nextInt(100000000) + "";
        Player me = new Player(true);
        me.SetColor(SerializableColor.getRandomColor());
        AddPlayer(me);

        level = Server.GetLevel();
        for(GameObject obj : level.getLevelBlocks())
        {
            objects.add(obj);
        }
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

        //objects.addAll(Server.GetTick());
        notMine.clear();
        notMine.addAll(Server.GetTick(name));
        notMine.addAll(objects);

        ArrayList<IGameObject> clonelist = (ArrayList<IGameObject>) ((ArrayList<IGameObject>) objects).clone();
        for (IGameObject object : clonelist)
        {
            object.Update();
            Server.UpdateTick(name, object);
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
        //level = new Level();
        gen = true;
    }

    public void ClearProjectile(Projectile p)
    {
        objects.remove(p);
    }

    public void EndMatch()
    {

    }


    public void Chat(String message, Player player)
    {
        Chat chat = new Chat(message, player);
        //chats.add(chat);
    }

    public void AddPlayer(Player p) throws RemoteException
    {
        playerMe = p;
        addGameObject(p);
    }

    public synchronized void addGameObject(GameObject go) throws RemoteException
    {
        objects.add(go);
        Server.SetTick(name, go);
    }

    public List<IGameObject> getObjects()
    {
        return objects;
    }

    public List<IGameObject> getAllObjects()
    {
        return notMine;
    }

    @Override
    public List<IGameObject> GetTick(String id) throws RemoteException
    {
        return null;
    }

    @Override
    public void SetTick(String id, IGameObject object) throws RemoteException
    {

    }

    @Override
    public void UpdateTick(String id, IGameObject object) throws RemoteException
    {

    }

    @Override
    public void DeleteTick(String id, IGameObject object) throws RemoteException
    {

    }

    @Override
    public Level GetLevel() throws RemoteException
    {
        return null;
    }
}
