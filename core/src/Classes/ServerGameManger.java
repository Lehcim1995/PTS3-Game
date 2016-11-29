package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializableColor;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.IRemotePublisherForListener;
import fontyspublisher.RemotePublisher;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by michel on 15-11-2016.
 */
public class ServerGameManger extends UnicastRemoteObject implements IGameManager
{

    private IRemotePublisherForDomain remotePublisherForDomain;
    private IRemotePublisherForListener remotePublisherForListener;
    private Registry registry;
    private transient Timer GameTicks;
    private transient TimerTask GameTickTask;

    private List<IGameObject> everything;
    private Map<String, List<IGameObject>> idObjects;

    private Random r = new Random();
    private Level level;

    public ServerGameManger() throws RemoteException
    {
        everything = new ArrayList<IGameObject>();
        idObjects = new HashMap<String, List<IGameObject>>(100);
        level = new Level();

        try
        {
            remotePublisherForDomain = new RemotePublisher();

            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(testBindingName, remotePublisherForDomain);
            registry.rebind(ServerManger, this);

        }
        catch (RemoteException ex)
        {
            System.out.println("Server: RemoteExeption " + ex.getMessage());
        }

        GameTicks = new Timer();
        GameTickTask = new TimerTask()
        {
            @Override
            public void run()
            {
//                try
//                {
//                    //remotePublisherForDomain.inform(UpdatePlayer, this, mygameObjects);
//                }
//                catch (RemoteException e)
//                {
//                    System.out.println("Server: RemoteExeption " + e.getMessage());
//                }
            }
        };
        GameTicks.scheduleAtFixedRate(GameTickTask, 0, (int) TICKLENGTH);
    }

    public static void main(String[] args)
    {
        try
        {
            ServerGameManger sgm = new ServerGameManger();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private <T> ArrayList<T> getObjectList(ArrayList<Object> list, Class<T> tocast)
    {
        ArrayList<T> returnList = new ArrayList<T>();

        for (Object go : list)
        {
            try
            {
                T igo = tocast.cast(go);

                returnList.add(igo);
            }
            catch (ClassCastException e)
            {
                System.out.println("Cast Error " + e.getMessage());
            }
        }

        return returnList;
    }

    @Override
    public List<IGameObject> GetTick(String id)
    {

        System.out.println("GetTick");
        return idObjects.get(id);
    }

    @Override
    public void SetTick(String id, IGameObject object)
    {
        System.out.println("incoming");

        if (!idObjects.containsKey(id))
        {
            idObjects.put(id, new ArrayList<IGameObject>());
            idObjects.get(id).addAll(everything);
        }

        everything.add(object);

        for (Map.Entry<String, List<IGameObject>> entry : idObjects.entrySet())
        {
            if (!entry.getKey().equals(id))
            {
                entry.getValue().add(object);
            }
        }
    }

    @Override
    public void UpdateTick(String id, IGameObject object) throws RemoteException
    {
        if (!idObjects.containsKey(id))
        {
            idObjects.put(id, new ArrayList<IGameObject>());
        }

        for (IGameObject go : everything)
        {
            if (go.getID() == object.getID())
            {
                go.SetPosition(object.GetPosition());
                go.SetRotation(object.GetRotation());
            }
        }

        for (Map.Entry<String, List<IGameObject>> entry : idObjects.entrySet())
        {
            if (!entry.getKey().equals(id))
            {
                for (IGameObject obj : entry.getValue())
                {
                    if(obj.getID() == object.getID())
                    {
                        obj.SetPosition(object.GetPosition());
                        obj.SetRotation(object.GetRotation());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void DeleteTick(String id, IGameObject object) throws RemoteException
    {

    }

    @Override
    public Level GetLevel() throws RemoteException
    {
        return level;
    }

    public IGameObject CreatePlayer(String name) throws RemoteException
    {
        Player p;
        p = new Player(false);
        p.SetName(name);
        p.SetColor(SerializableColor.getRandomColor());

        return p;
    }
}
