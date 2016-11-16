package Interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by michel on 15-11-2016.
 */
public interface IGameManager extends Remote
{

    String bindingName = "GameObjectsServer";
    String propertyName = "gameobjects";
    int portNumber = 1099;
    int fps = 30;
    float TICKLENGTH = 1000 / fps; // in milli

    List<IGameObject> GetTick() throws RemoteException;
    void SetTick(IGameObject object) throws RemoteException;;
}
