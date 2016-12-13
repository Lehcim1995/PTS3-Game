package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializableColor;
import Scenes.AbstractScreen;
import Scenes.GameSceneScreen;
import Scenes.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Json;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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
    private Map<Long, IGameObject> notMineMap;
    private boolean gen = false;
    private boolean online;

    private IGameManager Server;
    private Registry registry;
    private Timer GameTicks;
    private TimerTask GameTickTask;

    private Player playerMe;
    private AbstractScreen scene;

    private float tick = 0;

    private GameManager() throws RemoteException
    {

        //spectators = new ArrayList<Spectator>();
        killLogs = new ArrayList<>();
        //chats = new ArrayList<Chat>();
        objects = new ArrayList<>();
        notMine = new ArrayList<>();
        notMineMap = new HashMap<>(1000);

        InetAddress localhost = null;
        online = true;
        try
        {
            localhost = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            LOGGER.info("Client: UnknownHostException: " + e.getMessage());
            online = false;
        }
        String ip = ScreenManager.getInstance().getIp();//localhost.getHostAddress();
        int portNumber = ScreenManager.getInstance().getPortNumber();


        try
        {
            registry = LocateRegistry.getRegistry(ip, portNumber);

            Server = (IGameManager) registry.lookup(ServerManger);

        }
        catch (RemoteException ex)
        {
            LOGGER.info("Client: RemoteExeption " + ex.getMessage());
            online = false;
        }
        catch (NotBoundException e)
        {
            LOGGER.info("Client: NotBoundException " + e.getMessage());
            online = false;
        }
        Random r = new Random();
        name = ScreenManager.getInstance().getUser().GetName(); //r.nextInt(100000000) + "";
        System.out.println(name);
        Player me = new Player(true);
        me.setColor(SerializableColor.getRandomColor());
        addPlayer(me);

        if (online)
            level = Server.GetLevel();
        else
            level = new Level();

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
        //notMine.clear();
        if (online)
        {
            tick += Gdx.graphics.getDeltaTime();
            if (tick > 0.005f) //doe het elke zoveel seconden
            {
                List<IGameObject> tmp = Server.GetTick(name);

                for (IGameObject i : tmp) //add if absent
                {
                    notMineMap.putIfAbsent(i.getID(), i);
                    notMineMap.get(i.getID()).setPosition(i.getPosition());
                    notMineMap.get(i.getID()).setRotation(i.getRotation());
                }
                //als id in notmine niet in tmp staat haaldeze weg
                //TODO : maak dit korter en sneller

                //Werkt dit?
                //notMineMap.entrySet().retainAll(tmp);

                for (Iterator iterator = notMineMap.entrySet().iterator(); iterator.hasNext(); ) // remove when extra
                {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    boolean found = false;

                    for (IGameObject obj2 : tmp)
                    {
                        if (((IGameObject) pair.getValue()).getID() == obj2.getID())
                        {
                            found = true;
                        }
                    }

                    if (!found)
                    {
                        iterator.remove();
                    }
                }
            }

            notMine.addAll(notMineMap.values());
            notMine.addAll(objects);
        }

        ArrayList<IGameObject> clonelist = (ArrayList<IGameObject>) ((ArrayList<IGameObject>) objects).clone();
        for (IGameObject object : clonelist)
        {
            object.update();
            if (online)
                Server.UpdateTick(name, object);
        }

        ArrayList<GameObject[]> hitlist = new ArrayList<>();
        for (IGameObject go1 : notMine)
        {
            for (IGameObject go2 : notMine)
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
            golist[0].onCollisionEnter(golist[1]);
        }
    }

    public Player getPlayer()
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



    public void ClearProjectile(Projectile p) throws RemoteException
    {
        removeGameObject(p);
    }

    public void EndMatch()
    {

    }


    public void Chat(String message, Player player)
    {
        Chat chat = new Chat(message, player);
        //chats.add(chat);
    }

    public void addPlayer(Player p) throws RemoteException
    {
        System.out.println("Spawn");
        playerMe = p;
        addGameObject(p);
    }

    public synchronized void addGameObject(GameObject go) throws RemoteException
    {
        objects.add(go);
        if (online)
            Server.SetTick(name, go);
    }

    public void removeGameObject(GameObject go) throws RemoteException
    {
        objects.remove(go);
        if (online)
            Server.DeleteTick(name, go);
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
    public void DeleteUser(String id) throws RemoteException
    {

    }

    @Override
    public Level GetLevel() throws RemoteException
    {
        return null;
    }

    public String getName()
    {
        return name;
    }

    public AbstractScreen getScene()
    {
        return scene;
    }

    public void setScene(GameSceneScreen scene)
    {
        this.scene = scene;
    }

    public Camera getCamera()
    {
        return getScene().getCamera();
    }

    public void stop() throws RemoteException
    {
        Server.DeleteUser(name);
    }
}
