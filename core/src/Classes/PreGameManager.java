package Classes;

import Interfaces.IConnection;
import Interfaces.IGameManager;
import Interfaces.IServer;
import Interfaces.IUser;
import Scenes.ScreenManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Stefan on 12/20/2016.
 */
public class PreGameManager extends UnicastRemoteObject implements IServer
{
    private transient Registry registry;
    private IConnection connectionInstance = new Connection();
    private ArrayList<ServerGameManger> lobbies;

    public static void main(String[] args)
    {
        try
        {
            IServer pgm = new PreGameManager();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    public PreGameManager() throws RemoteException, UnknownHostException
    {
        this.lobbies = new ArrayList<>();

        registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(ServerManger, this);
        registry.rebind(connection, connectionInstance);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    @Override
    public ArrayList<String> getLobbies() throws RemoteException
    {
        ArrayList<String> lobbylist = new ArrayList<>();

        for (ServerGameManger lobby : lobbies) {
            lobbylist.add(lobby.getName());
        }
        return lobbylist;
    }

    @Override
    public IGameManager CreateLobby(String name) throws RemoteException
    {
        ServerGameManger sgm = null;
        try
        {
            sgm = new ServerGameManger(name);
            lobbies.add(sgm);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        return sgm;
    }

    @Override
    public IGameManager JoinLobby(String name, IUser user) throws RemoteException
    {
        for(ServerGameManger sgm : lobbies)
        {
            if(sgm.getName().equals(name))
            {
                ///TODO: Client side player toevoegen aan sgm
                return sgm;
            }
        }
        return null;
    }


}
