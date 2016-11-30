package Interfaces;

import Classes.Level;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by michel on 15-11-2016.
 */
public interface IGameManager extends Remote
{

    String ClientNewPlayer = "ClientNewPlayer";
    String ServerNewPlayer = "ServerNewPlayer";
    String UpdatePlayer = "UpdatePlayer";
    String GetPlayer = "getPlayer";
    String testBindingName = "GameObjectsServer";
    String remoteGameManger = "remoteGameManager";
    String propertyName = "gameobjects";
    String ServerManger = "serermanager";

    int portNumber = 1099;
    int fps = 60;
    float TICKLENGTH = 1000 / fps; // in milli

    List<IGameObject> GetTick(String id) throws RemoteException;
    void SetTick(String id, IGameObject object) throws RemoteException;

    void UpdateTick(String id, IGameObject object) throws RemoteException;

    void DeleteTick(String id, IGameObject object) throws RemoteException;

    Level GetLevel() throws RemoteException;
}
