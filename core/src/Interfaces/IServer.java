package Interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Stefan on 12/20/2016.
 */
public interface IServer extends Remote, Serializable
{
    int portNumber = 1099;
    String ServerManger = "serermanager";
    String connection = "connection";

    ArrayList<String> getLobbies() throws RemoteException;

    IGameManager CreateLobby(String name) throws RemoteException;

    IGameManager JoinLobby(String name, IUser user) throws RemoteException;


}
