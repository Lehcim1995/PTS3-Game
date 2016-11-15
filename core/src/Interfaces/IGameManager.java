package Interfaces;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michel on 15-11-2016.
 */
public interface IGameManager extends Serializable
{

    String bindingName = "GameObjectsServer";
    String propertyName = "gameobjects";
    int portNumber = 1099;
    int fps = 30;
    float TICKLENGTH = 1000 / fps; // in milli

    List<IGameObject> GetTick();
    void SetTick(IGameObject object);
}
