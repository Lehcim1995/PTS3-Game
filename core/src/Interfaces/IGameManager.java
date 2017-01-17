package Interfaces;

import Classes.Level;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michel on 15-11-2016.
 */
public interface IGameManager extends Remote, Serializable
{

    String ClientNewPlayer = "ClientNewPlayer";
    String ServerNewPlayer = "ServerNewPlayer";
    String UpdatePlayer = "UpdatePlayer";
    String GetPlayer = "getPlayer";
    String testBindingName = "GameObjectsServer";
    String remoteGameManger = "remoteGameManager";
    String propertyName = "gameobjects";
    String ServerManger = "serermanager";
    String connection = "connection";

    int portNumber = 1099;
    int fps = 60;
    float TICKLENGTH = 1000f / fps; // in milli

    List<IGameObject> GetTick(String id) throws RemoteException;

    void SetTick(String id, IGameObject object) throws RemoteException;

    void UpdateTick(String id, IGameObject object) throws RemoteException;

    void UpdateTick(String id, long objectId, Vector2 newPostion, float newRotation) throws RemoteException;

    void DeleteTick(String id, IGameObject object) throws RemoteException;

    void DeleteUser(String id) throws RemoteException;

    Level GetLevel() throws RemoteException;

    ArrayList<String> getUsers() throws RemoteException;

    void addUser(IUser user) throws RemoteException;

    void startMatch() throws RemoteException;


}
