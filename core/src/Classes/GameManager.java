package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import Interfaces.IServer;
import LibGDXSerialzableClasses.SerializableColor;
import Scenes.AbstractScreen;
import Scenes.GameSceneScreen;
import Scenes.ScreenEnum;
import Scenes.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

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
public class GameManager extends UnicastRemoteObject
{
    private static GameManager instance;
    private Level level;
    public static String chat = "";
    public static String killLog = "";
    private ArrayList<KillLog> killLogs;
    private String name;
    private List<IGameObject> objects;
    private List<IGameObject> notMine;
    private Map<Long, IGameObject> notMineMap;
    private List<Chat> chats;
    private List<Chat> chatsOnline;
    private boolean gen = false;
    private boolean online;
    private IGameManager server;
    private transient Registry registry;
    private Player playerMe;
    private Spectator spectatorMe;
    private AbstractScreen scene;
    private float tick = 0;
    private boolean isStoped;

    private GameManager() throws RemoteException
    {
        constructor();
    }

    private void constructor() throws RemoteException
    {
        killLogs = new ArrayList<>();
        objects = new ArrayList<>();
        notMine = new ArrayList<>();
        notMineMap = new HashMap<>(1000);
        chats = new ArrayList<>();
        chatsOnline = new ArrayList<>();

        InetAddress localhost = null;
        online = true;
        try
        {
            localhost = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            //Log this
            LOGGER.log(java.util.logging.Level.SEVERE, "Client: UnknownHostException: " + e.getMessage());
            online = false;
        }
        String ip = ScreenManager.getInstance().getIp();
        int portNumber = ScreenManager.getInstance().getPortNumber();

        try
        {
            registry = LocateRegistry.getRegistry(ip, portNumber);
            String serverManger = "serermanager";
            //server = (IGameManager) registry.lookup(ServerManger); //TODO dit geeft een IServer terug geen IGamemanger
            IServer tempserver = (IServer) registry.lookup(serverManger);
            //not bound exception
            String serverName = ScreenManager.getInstance().getLobbyname();
            server = tempserver.JoinLobby(serverName, ScreenManager.getInstance().getUser());


        }
        catch (RemoteException e)
        {
            //Log this
            LOGGER.log(java.util.logging.Level.SEVERE, "Client: RemoteExeption: " + e.getMessage());
            online = false;
        }
        catch (NotBoundException e)
        {
            //Log this
            LOGGER.log(java.util.logging.Level.SEVERE, "Client: NotBoundException: " + e.getMessage());
            online = false;
        }
        name = ScreenManager.getInstance().getUser().getName();
        LOGGER.log(java.util.logging.Level.INFO, "User name: " + name);

        //Check if spectator if not make Player else make Spectator
        if (ScreenManager.getInstance().getIsSpectator())
        {
            Spectator me = new Spectator(ScreenManager.getInstance().getUser().getName());
            addSpectator(me);
        }
        else
        {
            Player me = new Player(true);
            me.setColor(SerializableColor.getRandomColor());
            addPlayer(me);
        }
        if (online)
        {
            level = server.GetLevel();
        }
        else
        {
            level = new Level();
        }

        for (GameObject obj : level.getLevelBlocks())
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
        catch (RemoteException e)
        {
            //Log this
            LOGGER.log(java.util.logging.Level.SEVERE, "getInstance Error " + e.getMessage());
        }
        return instance;
    }
    /**
     * Timed Update
     *
     */
    public void Update() throws RemoteException
    {
        if (!gen)
        {
            StartMatch();
        }

        notMine.clear();
        chatsOnline.clear();
        killLogs.clear();
        if (online)
        {
            tick += Gdx.graphics.getDeltaTime();
            if (tick > 0.005f) //doe het elke zoveel seconden
            {
                tick = 0;
                List<IGameObject> tmp = new ArrayList<>(server.GetTick(name));

                for (IGameObject i : tmp) //add if absent
                {
                    notMineMap.putIfAbsent(i.getID(), i);
                    notMineMap.get(i.getID()).setPosition(i.getPosition());
                    notMineMap.get(i.getID()).setRotation(i.getRotation());
                }

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



        chatsOnline.addAll(getObjectList(notMine, Chat.class));
        killLogs.addAll(getObjectList(notMine, KillLog.class));
        chatsOnline.sort(Comparator.comparingDouble(chat1 -> -chat1.getBorn()));
        killLogs.sort(Comparator.comparingDouble(killlog1 -> -killlog1.getBorn()));

        if (getObjectList(notMine, StopGameObject.class).size() > 0 )
        {
            System.out.println("Stop");
            //ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
            online = false;
            isStoped = true;
            //return;
        }

        ArrayList<IGameObject> clonelist = (ArrayList<IGameObject>) ((ArrayList<IGameObject>) objects).clone();
        for (IGameObject object : clonelist)
        {
            object.update();
            if (online)
            {
                server.UpdateTick(name, object);
            }
        }

        ArrayList<GameObject[]> hitlist = new ArrayList<>();
        for (IGameObject go1 : notMine)
        {
            for (IGameObject go2 : notMine)
            {
                if (go1 != go2 && go1.isHit(go2))
                {
                    hitlist.add(new GameObject[]{(GameObject) go1, (GameObject) go2});
                }
            }
        }

        for (GameObject[] golist : hitlist)
        {
            golist[0].onCollisionEnter(golist[1]);
        }
    }

    private <T> ArrayList<T> getObjectList(List<IGameObject> list, Class<T> tClass)
    {
        ArrayList<T> returnList = new ArrayList<>();

        for (Object go : list)
        {
            try
            {
                T igo;
                if (go.getClass() == tClass)
                {
                    igo = (T) go;
                    returnList.add(igo);
                }

            }
            catch (ClassCastException e)
            {
                //Log this
                LOGGER.log(java.util.logging.Level.SEVERE, "Cast Error " + e.getMessage());
            }
            catch (NullPointerException e)
            {
                //Log this
                LOGGER.log(java.util.logging.Level.SEVERE, "Null Error " + e.getMessage());
            }
        }

        return returnList;
    }
    /**
     * Get the Me Player
     *
     */
    public Player getPlayer()
    {
        return playerMe;
    }
    /**
     * Spawn a Player
     *
     * @param player    - Player to be spawned
     */
    public void SpawnPlayer(Player player)
    {
        player.Spawn();
    }
    /**
     * Starts the Match of a lobby
     *
     */
    public void StartMatch() throws RemoteException
    {
        gen = true;
        isStoped =false;
        constructor();
    }
    /**
     * Removes a projectile
     *
     * @param p    - The projectile to be removed
     */
    public void ClearProjectile(Projectile p) throws RemoteException
    {
        removeGameObject(p);
    }
    //TODO: Make use of the Method EndMatch. Still needs to be filled in.
    public void EndMatch()
    {

    }
    /**
     * Adds Player to the game.
     *
     * @param p    - Player to be added to the game
     */
    public void addPlayer(Player p) throws RemoteException
    {
        System.out.println("Spawn Player");
        playerMe = p;
        playerMe.setName(name);
        addGameObject(p);
    }
    /**
     * gets the Spectator
     *
     */
    public Spectator getSpectator()
    {
        return spectatorMe;
    }
    /**
     * Sets the Spectator
     *
     */
    public void setSpectator(Spectator spectator)
    {
        this.spectatorMe = spectator;
    }
    /**
     * Adds a Spectator
     *
     */
    public void addSpectator(Spectator s) throws RemoteException
    {
        System.out.println("Spawn Spectator");
        spectatorMe = s;
        objects.add(s);
        name = "Spectator";
    }
    /**
     * Add gameObject to the scene
     *
     * @param go    - Game object to be added to the scene
     */
    public synchronized void addGameObject(GameObject go) throws RemoteException
    {
        objects.add(go);
        if (online)
        {
            server.SetTick(name, go);
        }
    }
    /**
     * Removes a specific object from the game
     *
     * @param go    - Object to be removed
     */
    public void removeGameObject(GameObject go) throws RemoteException
    {
        objects.remove(go);
        if (online)
        {
            server.DeleteTick(name, go);
        }
    }

    public List<IGameObject> getObjects()
    {
        return objects;
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
    /**
     * Called when a player leaves the game.
     *
     */
    public void stop() throws RemoteException
    {
        server.DeleteUser(name);
    }

    public List<Chat> getChats()
    {
        return chatsOnline;
    }

    public List<KillLog> getKillLogs()
    {
        return killLogs;
    }
    /**
     * Clears the chat
     *
     */
    public void clearChat(Chat chat) throws RemoteException
    {
        chats.remove(chat);
        if (online) removeGameObject(chat);
    }

    public List<IGameObject> getAllObjects()
    {
        return notMine;
    }

    /**
     * Gets a Player to be spectated by a spectator
     *
     */
    public List<Player> GetSpectatedPlayer()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        for (IGameObject go : objects)
            if (go instanceof Player)
            {
                playerList.add((Player) go);
            }
        return playerList;

    }

    public boolean IsStopped()
    {
        return isStoped;
    }
    /**
     * Adds a new KillLog
     *
     * @param kl    - KillLog to be added to the KillLog list
     */
    public void AddKillLog(KillLog kl)
    {
        killLogs.add(kl);
    }
}
