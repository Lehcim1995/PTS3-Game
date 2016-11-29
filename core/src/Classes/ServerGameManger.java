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
public class ServerGameManger extends UnicastRemoteObject implements IGameManager, IRemotePropertyListener
{

    private IRemotePublisherForDomain remotePublisherForDomain;
    private IRemotePublisherForListener remotePublisherForListener;
    private Registry registry;
    private transient Timer GameTicks;
    private transient TimerTask GameTickTask;

    private List<IGameObject> mygameObjects;
    private Map<String, IGameObject> playerList;

    private Random r = new Random();

    public ServerGameManger() throws RemoteException
    {
        mygameObjects = new ArrayList<IGameObject>();
        playerList = new HashMap<String, IGameObject>(10);

        try
        {
            remotePublisherForDomain = new RemotePublisher();

            remotePublisherForDomain.registerProperty(propertyName);
            remotePublisherForDomain.registerProperty(remoteGameManger);
            remotePublisherForDomain.registerProperty(ClientNewPlayer);
            remotePublisherForDomain.registerProperty(UpdatePlayer);
            remotePublisherForDomain.registerProperty(ServerNewPlayer);
            remotePublisherForDomain.registerProperty(GetPlayer);

            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(testBindingName, remotePublisherForDomain);
            registry.rebind("", this);

            remotePublisherForListener = (IRemotePublisherForListener) remotePublisherForDomain;
            remotePublisherForListener.subscribeRemoteListener(this, propertyName);
            remotePublisherForListener.subscribeRemoteListener(this, ClientNewPlayer);
            remotePublisherForListener.subscribeRemoteListener(this, GetPlayer);

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
                try
                {
                    remotePublisherForDomain.inform(UpdatePlayer, this, mygameObjects);
                }
                catch (RemoteException e)
                {
                    System.out.println("Server: RemoteExeption " + e.getMessage());
                }
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
    public List<IGameObject> GetTick()
    {

        System.out.println("GetTick");
        return mygameObjects;
    }

    @Override
    public void SetTick(IGameObject object)
    {
        mygameObjects.add(object);
    }

    public IGameObject CreatePlayer(String name) throws RemoteException
    {
        Player p;
        p = new Player(false);
        p.SetName(name);
        p.SetColor(SerializableColor.getRandomColor());

        return p;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException
    {

        if (propertyChangeEvent.getPropertyName().equals(ClientNewPlayer))
        {
            //TODO use playername

            String name = (String) propertyChangeEvent.getNewValue();
            name += r.nextInt(100000);

            Player p;
            System.out.println("Server Create Player " + name);
            p = new Player(false);
            p.SetName(name);
            p.SetColor(SerializableColor.getRandomColor());

            playerList.put(name, p);
            mygameObjects.add(p);

            remotePublisherForDomain.inform(ServerNewPlayer, playerList, p);
        }



        if (propertyChangeEvent.getPropertyName().equals(GetPlayer))
        {

            //TODO Use playername;
            Player p = (Player) propertyChangeEvent.getNewValue();
            //System.out.println("Server Update Player " + p.GetName());

            Player updateplayer = (Player) playerList.get(p.GetName());
            updateplayer.SetPosition(p.GetPosition());
            updateplayer.SetRotation(p.GetRotation());
        }
    }
}
