package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.RemotePublisher;
import sun.applet.Main;

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
public class ServerGameManger implements IGameManager
{

    private IRemotePublisherForDomain remotePublisherForDomain;
    private Registry registry;
    private Timer GameTicks;
    private TimerTask GameTickTask;

    private List<IGameObject> mygameObjects;
    private Color color;

    public ServerGameManger()
    {
        mygameObjects = new ArrayList<IGameObject>();

        try
        {
            color = Color.RED;
            remotePublisherForDomain = new RemotePublisher();
            remotePublisherForDomain.registerProperty(propertyName);
            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(bindingName, remotePublisherForDomain);
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
        return null;
    }

    @Override
    public void SetTick(IGameObject object)
    {

    }
}
