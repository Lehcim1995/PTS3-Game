package Classes;

import Scenes.ScreenEnum;
import Scenes.ScreenManager;
import com.badlogic.gdx.Game;

/**
 * Created by Nick on 22-11-2016.
 */
public class ASOM extends Game
{
    @Override
    public void create()
    {
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN);
    }
}
