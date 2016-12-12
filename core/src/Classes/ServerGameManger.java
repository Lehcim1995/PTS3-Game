package Classes;

import Interfaces.IConnection;
import Interfaces.IGameManager;
import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializableColor;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private Registry registry;

    private List<IGameObject> everything;
    private Map<String, List<IGameObject>> idObjects;
    private IConnection connectionInstance = new Connection();

    private Random r = new Random();
    private Level level;

    public ServerGameManger() throws RemoteException
    {
        everything = new ArrayList<>();
        idObjects = new HashMap<>(100);
        level = new Level();

        try
        {

            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(ServerManger, this);
            registry.rebind(connection, connectionInstance);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
//            IUser user = connectionInstance.LogIn("hans@email.com", "cactus");
//            if(user !=null)
//            {
//                System.out.println("gelukt" + user);
//            }
//            else
//            {
//                System.out.println("gefaalt" );
//            }

        }
        catch (RemoteException ex)
        {
            System.out.println("Server: RemoteExeption " + ex.getMessage());
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Server: RemoteExeption " + ex.getMessage());
        }
//        catch (SQLException e)
//        {
//            System.out.println("Server: sql Exception: " + e.getMessage());
//        }

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
        return idObjects.get(id);
    }

    @Override
    public void SetTick(String id, IGameObject object)
    {
        System.out.println("New Object From : " + id);

        idObjects.putIfAbsent(id, everything); //Waaneer id niet bestaat voeg alles toe aan die speler

        everything.add(object); // voeg nieuw object toe aan iedereen
        idObjects.entrySet().stream().filter(entry -> !entry.getKey().equals(id)).forEach(entry -> entry.getValue().add(object)); //voeg object toe aan iedereen behalve ik
    }

    @Override
    public void UpdateTick(String id, IGameObject object) throws RemoteException
    {
        System.out.println("Update Object From : " + id);

        //Update de position voor iedereen
        for (IGameObject go : everything)
        {
            if (go.getID() == object.getID())
            {
                go.setPosition(object.getPosition());
                go.setRotation(object.getRotation());
                break;
            }
        }

        //Update de position voor iedereen behalve mij
        for (Map.Entry<String, List<IGameObject>> entry : idObjects.entrySet())
        {
            if (!entry.getKey().equals(id))
            {
                for (IGameObject obj : entry.getValue())
                {
                    if (obj.getID() == object.getID())
                    {
                        obj.setPosition(object.getPosition());
                        obj.setRotation(object.getRotation());
                        break;
                    }
                }
            }
        }
    }

    private void AddForEveryOne()
    {

    }

    private void AddForEveryOneButMe()
    {

    }

    private void AddForMe()
    {

    }

    private void RemoveForEveryOne()
    {

    }

    private void RemoveForEveryOneButMe()
    {

    }

    private void RemoveForMe()
    {

    }

    @Override
    public void DeleteTick(String id, IGameObject object) throws RemoteException
    {
        //haald object overal weg waar hij bestaat
        idObjects.entrySet().forEach(stringListEntry -> stringListEntry.getValue().removeIf(gameObject -> gameObject.getID() == object.getID()));
        everything.removeIf(gameObject -> gameObject.getID() == object.getID());

    }

    @Override
    public void DeleteUser(String id)
    {
        //delete alles van een user
        idObjects.entrySet().forEach(set -> everything.removeIf(obj -> obj.getID() == ((IGameObject) set.getValue()).getID()));
        idObjects.entrySet().removeIf(keyid -> Objects.equals(keyid.getKey(), id));
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
        p.setName(name);
        p.setColor(SerializableColor.getRandomColor());

        return p;
    }
}
