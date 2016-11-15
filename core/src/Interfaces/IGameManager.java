package Interfaces;

import java.util.List;

/**
 * Created by michel on 15-11-2016.
 */
public interface IGameManager
{
    List<IGameObject> GetTick();
    void SetTick(IGameObject object);
}
