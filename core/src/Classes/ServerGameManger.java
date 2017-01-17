package Classes;

import Interfaces.IGameManager;
import Interfaces.IGameObject;
import Interfaces.IUser;
import LibGDXSerialzableClasses.SerializableColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by michel on 15-11-2016.
 */
public class ServerGameManger extends UnicastRemoteObject implements IGameManager
{
    private String name;
    private List<IGameObject> everything;
    private Map<String, List<IGameObject>> idObjects; //TODO gebruik list voor het opslaan voor eigen objecten niet voor opslaan van objecten die voor mij bedoelt zijn.
    private ArrayList<User> userList;
    private ArrayList<String> stringUserList;

    private Random r = new Random();
    private Level level;

    private float matchTime = 5000; //5 * 60 * 1000;
    private Timer matchTimer;
    private boolean matchStarted;
    private PreGameManager pgm;

    public ServerGameManger() throws RemoteException
    {
        Constructor();

    }

    public ServerGameManger(String name) throws RemoteException
    {
        Constructor();
        this.name = name;
    }

    public ServerGameManger(String name, PreGameManager preGameManager) throws RemoteException
    {
        Constructor();
        this.name = name;
        pgm = preGameManager;
    }


    private <T> ArrayList<T> getObjectList(ArrayList<Object> list, Class<T> tocast)
    {
        ArrayList<T> returnList = new ArrayList<>();

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
        if (id.equals("Spectator"))
        {
            return GetEverything();
        }

        List<IGameObject> allbutmeobject = new ArrayList<>();

        for (Map.Entry<String, List<IGameObject>> objlist : idObjects.entrySet())
        {
            if (!objlist.getKey().equals(id))
            {
                //System.out.println("Adding from " + objlist.getKey() + " TO " + id);
                allbutmeobject.addAll(objlist.getValue());
            }
        }

        allbutmeobject.addAll(everything);

        return allbutmeobject;
    }

    public List<IGameObject> GetEverything()
    {
        List<IGameObject> alllist = new ArrayList<>();

        for (Map.Entry<String, List<IGameObject>> objlist : idObjects.entrySet())
        {
            alllist.addAll(objlist.getValue());
        }

        return alllist;
    }

    @Override
    public void SetTick(String id, IGameObject object)
    {
        //System.out.println("New Object From : " + id);
        /*
        idObjects.putIfAbsent(id, new ArrayList<>(everything)); //Waaneer id niet bestaat voeg alles toe aan die speler

        everything.add(object); // voeg nieuw object toe aan iedereen
        idObjects.entrySet().stream().filter(entry -> !entry.getKey().equals(id)).forEach(entry -> entry.getValue().add(object)); //voeg object toe aan iedereen behalve ik
        */
        if(!matchStarted){
            try
            {
                startMatch();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        idObjects.putIfAbsent(id, new ArrayList<>());
        idObjects.get(id).add(object);
    }

    @Override
    public void UpdateTick(String id, IGameObject object) throws RemoteException
    {
        //System.out.println("Update Object From : " + id);
        /*
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
        }*/

        for(IGameObject obj : idObjects.get(id))
        {
            if (obj.getID() == object.getID())
            {
                obj.setPosition(object.getPosition());
                obj.setRotation(object.getRotation());
                break;
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

    private void Constructor() throws RemoteException
    {
        everything = new ArrayList<>();
        userList = new ArrayList<>();
        stringUserList = new ArrayList<>();
        idObjects = new HashMap<>(100);
        level = new Level();
        matchTimer = new Timer();

    }

    @Override
    public void DeleteTick(String id, IGameObject object) throws RemoteException
    {
        //haald object overal weg waar hij bestaat
        //idObjects.entrySet().forEach(stringListEntry -> stringListEntry.getValue().removeIf(gameObject -> gameObject.getID() == object.getID()));
        //everything.removeIf(gameObject -> gameObject.getID() == object.getID());

        idObjects.get(id).removeIf(obj -> obj.getID() == object.getID());

    }

    @Override
    public void DeleteUser(String id)
    {
        //delete alles van een user
        //idObjects.entrySet().forEach(set -> everything.removeIf(obj -> obj.getID() == ((IGameObject) set.getValue()).getID()));
        //idObjects.entrySet().removeIf(keyid -> Objects.equals(keyid.getKey(), id));

        idObjects.remove(id);
    }

    @Override
    public Level GetLevel() throws RemoteException
    {
        return level;
    }

    @Override
    public ArrayList<String> getUsers() throws RemoteException
    {
        stringUserList.clear();
        for (User u : userList)
        {
            stringUserList.add(u.getName());
        }
        return stringUserList;
    }

    @Override
    public void addUser(IUser user) throws RemoteException
    {
        userList.add((User) user);
    }

    @Override
    public void startMatch() throws RemoteException
    {
        System.out.println("Match start");
        final ServerGameManger me = this;
        matchTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("game over bitch!!");
                matchTimer.cancel();

                try
                {
                    StopGameObject sgo = new StopGameObject();
                    everything.add(sgo);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }

                pgm.StopLobby(me.getName());
            }
        },(long) matchTime, (long) matchTime);
        matchStarted = true;
    }

    public IGameObject CreatePlayer(String name) throws RemoteException
    {
        Player p;
        p = new Player(false);
        p.setName(name);
        p.setColor(SerializableColor.getRandomColor());

        return p;
    }

    public String getName()
    {
        return name;
    }

    public void addUser(User user)
    {
        userList.add(user);
    }
}
