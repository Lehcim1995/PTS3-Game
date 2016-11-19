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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;

    public ServerGameManger() throws RemoteException
    {
        mygameObjects = new ArrayList<IGameObject>();

        try
        {
            remotePublisherForDomain = new RemotePublisher();

            remotePublisherForDomain.registerProperty(propertyName);
            remotePublisherForDomain.registerProperty(remoteGameManger);
            remotePublisherForDomain.registerProperty(ClientNewPlayer);
            remotePublisherForDomain.registerProperty(UpdatePlayer);
            remotePublisherForDomain.registerProperty(ServerNewPlayer);

            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(testBindingName, remotePublisherForDomain);

            remotePublisherForListener = (IRemotePublisherForListener) remotePublisherForDomain;
            remotePublisherForListener.subscribeRemoteListener(this, propertyName);
            remotePublisherForListener.subscribeRemoteListener(this, ClientNewPlayer);

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
                    if (player1 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 1");
                    }
                    if (player2 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 2");
                    }
                    if (player3 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 3");
                    }
                    if (player4 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 4");
                    }
                    if (player5 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 5");
                    }
                    if (player6 != null)
                    {
                        remotePublisherForDomain.inform(UpdatePlayer, null, mygameObjects);
                        //System.out.println("Update Player 6");
                    }

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
        return null;
    }

    @Override
    public void SetTick(IGameObject object)
    {
        System.out.println("SetTick");
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException
    {

        if (propertyChangeEvent.getPropertyName().equals(ClientNewPlayer))
        {
            if (player1 == null)
            {
                System.out.println("Server Create Player 1");
                player1 = new Player(false);
                player1.SetName("Player1");
                player1.SetColor(SerializableColor.BLACK);
                mygameObjects.add(player1);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
                //TODO FIX dit
            }
            else if (player2 == null)
            {
                System.out.println("Server Create Player 2");
                player2 = new Player(false);
                player2.SetName("Player2");
                player2.SetColor(SerializableColor.RED);
                mygameObjects.add(player2);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
            }
            else if (player3 == null)
            {
                System.out.println("Server Create Player 3");
                player3 = new Player(false);
                player3.SetName("Player3");
                player3.SetColor(SerializableColor.BLUE);
                mygameObjects.add(player3);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
            }
            else if (player4 == null)
            {
                System.out.println("Server Create Player 4");
                player4 = new Player(false);
                player4.SetName("Player4");
                player4.SetColor(SerializableColor.YELLOW);
                mygameObjects.add(player4);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
            }
            else if (player5 == null)
            {
                System.out.println("Server Create Player 5");
                player5 = new Player(false);
                player5.SetName("Player5");
                player5.SetColor(SerializableColor.GREEN);
                mygameObjects.add(player5);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
            }
            else if (player6 == null)
            {
                System.out.println("Server Create Player 6");
                player6 = new Player(false);
                player6.SetName("Player6");
                player6.SetColor(SerializableColor.GOLD);
                mygameObjects.add(player6);
                remotePublisherForDomain.inform(ServerNewPlayer, null, mygameObjects);
            }
        }

        if (propertyChangeEvent.getPropertyName().equals(UpdatePlayer))
        {
            Player p = (Player) propertyChangeEvent.getNewValue();

            if (p.GetName().equals("player1"))
            {
                player1.SetPosition(p.GetPosition());
            }

        }
    }
}
