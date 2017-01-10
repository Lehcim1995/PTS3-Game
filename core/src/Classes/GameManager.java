package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import Interfaces.IServer;
import LibGDXSerialzableClasses.SerializableColor;
import Scenes.AbstractScreen;
import Scenes.GameSceneScreen;
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
import java.util.logging.Logger;

/**
 * Created by Nick on 11-10-2016.
 */
public class GameManager extends UnicastRemoteObject
{
    private static GameManager instance;
    private final Level level;
    public String chat = "";
    private ArrayList<KillLog> killLogs;
    private String name;
    private List<IGameObject> objects;
    private List<IGameObject> notMine;
    private Map<Long, IGameObject> notMineMap;
    private List<Chat> chats;
    private List<Chat> chatsOnline;
    private boolean gen = false;
    private boolean online;
    private IGameManager Server;
    private Registry registry;
    private Player playerMe;
    private Spectator spectatorMe;
    private AbstractScreen scene;
    private float tick = 0;
    private Logger logger;

    private GameManager() throws RemoteException
    {

        logger = Logger.getAnonymousLogger();
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
            logger.info("Client: UnknownHostException: " + e.getMessage());
            online = false;
        }
        String ip = ScreenManager.getInstance().getIp();//localhost.getHostAddress();
        int portNumber = ScreenManager.getInstance().getPortNumber();

        try
        {
            registry = LocateRegistry.getRegistry(ip, portNumber);
            String ServerManger = "serermanager";
            //Server = (IGameManager) registry.lookup(ServerManger); //TODO dit geeft een IServer terug geen IGamemanger
            IServer tempserver = (IServer) registry.lookup(ServerManger);
            //not bound exception
            String servername = ScreenManager.getInstance().getLobbyname();
            System.out.println("ServerName: " + servername);
            Server = tempserver.JoinLobby(servername, ScreenManager.getInstance().getUser());


        }
        catch (RemoteException ex)
        {
            logger.info("Client: RemoteExeption " + ex.getMessage());
            online = false;
        }
        catch (NotBoundException e)
        {
            logger.info("Client: NotBoundException " + e.getMessage());
            online = false;
        }
        Random r = new Random();
        name = ScreenManager.getInstance().getUser().getName(); //r.nextInt(100000000) + "";
        System.out.println(name);

        //todo: MIGHT NEED FIX
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
        if (online) level = Server.GetLevel();
        else level = new Level();

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
        catch (RemoteException ex)
        {
            //logger.info("Client Remote error " + ex.getMessage());
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
        chatsOnline.clear();
        if (online)
        {
            tick += Gdx.graphics.getDeltaTime();
            if (tick > 0.005f) //doe het elke zoveel seconden
            {
                tick = 0;
                List<IGameObject> tmp = Server.GetTick(name);

                for (IGameObject i : tmp) //add if absent
                {
                    /*if (i.getClass() == Chat.class )
                    {
                        chatsOnline.add((Chat) i);
                    }
                    else*/
                    {
                        notMineMap.putIfAbsent(i.getID(), i);
                        notMineMap.get(i.getID()).setPosition(i.getPosition());
                        notMineMap.get(i.getID()).setRotation(i.getRotation());
                    }
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

        chatsOnline.addAll(getObjectList(notMine, Chat.class));
        chatsOnline.sort(Comparator.comparingDouble((chat1) -> -chat1.getBorn()));

        ArrayList<IGameObject> clonelist = (ArrayList<IGameObject>) ((ArrayList<IGameObject>) objects).clone();
        for (IGameObject object : clonelist)
        {
            object.update();
            if (online) Server.UpdateTick(name, object);
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
                System.out.println("Cast Error " + e.getMessage());
            }
        }

        return returnList;
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

    public void addPlayer(Player p) throws RemoteException
    {
        System.out.println("Spawn Player");
        playerMe = p;
        playerMe.setName(name);
        addGameObject(p);
    }
    public Spectator getSpectator()
    {
        return spectatorMe;
    }
    public void setSpectator(Spectator spectator)
    {
        this.spectatorMe = spectator;
    }

    public void addSpectator(Spectator s) throws RemoteException
    {
        System.out.println("Spawn Spectator");
        spectatorMe = s;
        addGameObject(s);
    }

    public synchronized void addGameObject(GameObject go) throws RemoteException
    {
        //if (go.getClass() == Chat.class)
        //{
        //    chats.add((Chat) go);
        //}
        //else
        //{
            objects.add(go);
        //}
        if (online) Server.SetTick(name, go);
    }

    public void removeGameObject(GameObject go) throws RemoteException
    {
        objects.remove(go);
        if (online) Server.DeleteTick(name, go);
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

    public void stop() throws RemoteException
    {
        Server.DeleteUser(name);
    }

    public List<Chat> getChats()
    {
        return chatsOnline;
    }

    public void clearChat(Chat chat) throws RemoteException
    {
        chats.remove(chat);
        if (online) removeGameObject(chat);
    }

    public List<IGameObject> getAllObjects()
    {
        return notMine;
    }


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

    public void AddKillLog(KillLog kl)
    {
        killLogs.add(kl);
    }
}
