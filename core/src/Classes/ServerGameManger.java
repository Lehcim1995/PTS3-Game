package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.IRemotePublisherForListener;
import fontyspublisher.RemotePublisher;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michel on 15-11-2016.
 */
public class ServerGameManger implements IGameManager, IRemotePropertyListener
{

    private IRemotePublisherForDomain remotePublisherForDomain;
    private IRemotePublisherForListener remotePublisherForListener;
    private Registry registry;
    private transient Timer GameTicks;
    private transient TimerTask GameTickTask;

    private List<IGameObject> mygameObjects;
    private Color color;

    public ServerGameManger()
    {
        mygameObjects = new ArrayList<IGameObject>();

        try
        {
            color = SerializableColor.RED;
            remotePublisherForDomain = new RemotePublisher();
            remotePublisherForDomain.registerProperty(propertyName);
            remotePublisherForDomain.registerProperty(remoteGameManger);
            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(testBindingName, remotePublisherForDomain);
            remotePublisherForListener = (IRemotePublisherForListener) remotePublisherForDomain;
            remotePublisherForListener.subscribeRemoteListener(this, remoteGameManger);
            //TODO fix dat de remote shit niet op de server wordt aangeroepen

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
                    float r = MathUtils.random();
                    float g = MathUtils.random();
                    float b = MathUtils.random();
                    float a = MathUtils.random();

                    color = new SerializableColor(r,g,b,a);
                    remotePublisherForDomain.inform(propertyName , null, color);
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
        ServerGameManger sgm = new ServerGameManger();
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
        String s = (String) propertyChangeEvent.getNewValue();

        System.out.println("Server " + s);
    }
}
