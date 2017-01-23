package Server;

import Classes.Connection;
import Interfaces.IConnection;
import Interfaces.IGameManager;
import Interfaces.IServer;
import Interfaces.IUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Stefan on 12/20/2016.
 */
public class PreGameManager extends UnicastRemoteObject implements IServer
{
    private transient Registry registry;
    private IConnection connectionInstance = new Connection();
    private ArrayList<ServerGameManger> lobbies;

    public Connection getConnectionInstance()
    {
        return (Connection) connectionInstance;
    }

    public PreGameManager() throws RemoteException, UnknownHostException
    {
        this.lobbies = new ArrayList<>();

        registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(ServerManger, this);
        registry.rebind(connection, connectionInstance);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    public static void main(String[] args)
    {
        try
        {
            IServer pgm = new PreGameManager();
        }
        catch (RemoteException | UnknownHostException e)
        {
            e.printStackTrace();
        }

        while (true)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
            try
            {
                input = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (input.equals("Exit"))
            {
                break;
            }
        }

    }

    @Override
    public ArrayList<String> getLobbies() throws RemoteException
    {
        ArrayList<String> lobbylist = new ArrayList<>();

        for (ServerGameManger lobby : lobbies)
        {
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
            sgm = new ServerGameManger(name, this);
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
        for (ServerGameManger sgm : lobbies)
        {
            if (sgm.getName().equals(name))
            {
                ///TODO: Client side player toevoegen aan sgm
                return sgm;
            }
        }
        return null;
    }
    public void LeaveLobby(String lobbyname, String username) throws RemoteException
    {
        for (ServerGameManger sgm : lobbies)
        {
            if (sgm.getName().equals(lobbyname))
            {
               sgm.DeleteUser(username);
            }
        }
    }

    public void StopLobby(String name)
    {
        lobbies.removeIf(lb -> Objects.equals(lb.getName(), name));
    }


}
