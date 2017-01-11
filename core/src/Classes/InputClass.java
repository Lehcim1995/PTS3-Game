package Classes;

import Scenes.ScreenManager;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by michel on 27-9-2016.
 */
public class InputClass implements InputProcessor
{
    private final float holdDownTimer = 15;
    private Player player;
    private HashMap<Integer, Boolean> KeymapHold;
    private HashMap<Integer, Date> KeymapUp;
    private HashMap<Integer, Date> KeymapDown;
    private Timer timer;
    private boolean chating;

    public InputClass(Player p)
    {
        KeymapHold = new HashMap<Integer, Boolean>();
        player = p;
    }

    public boolean GetKey(int code)
    {
        return KeymapHold.get(code) == null ? false : KeymapHold.get(code).booleanValue();
    }

    public boolean GetKeyUp(int code)
    {
        return false;
    }

    public boolean GetKeyDown(int code)
    {
        return false;
        //return KeymapDown.get(code).getTime();
    }

    @Override
    public boolean keyDown(int keycode)
    {

        if (!chating) KeymapHold.put(keycode, true);
        if (ScreenManager.getInstance().getIsSpectator())
        {
            if (keycode == Input.Keys.LEFT)
            {
                GameManager.getInstance().getSpectator().NextPlayer();
            }
            if (keycode == Input.Keys.RIGHT)
            {
                GameManager.getInstance().getSpectator().PrevPlayer();
            }
        }
        //KeymapDown.put(keycode, Calendar.getInstance().getTime());
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (chating && keycode == Input.Keys.ENTER)
        {
            chating = false;
            Chat c;
            try
            {
                c = new Chat(GameManager.getInstance().chat, player);
                GameManager.getInstance().addGameObject(c);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }

            GameManager.getInstance().chat = "";
        }

        if (chating && keycode == Input.Keys.BACKSPACE)
        {
            String temp = GameManager.getInstance().chat;

            if (!temp.isEmpty())
            {
                if (temp.length() < 2)
                {
                    GameManager.getInstance().chat = "";
                }
                else
                {
                    GameManager.getInstance().chat = temp.substring(0, temp.length() - 2);
                }
            }
        }

        if (keycode == Input.Keys.T)
        {
            chating = true;
        }

        KeymapHold.put(keycode, false);
        //KeymapUp.put(keycode, Calendar.getInstance().getTime());
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        if (chating) GameManager.getInstance().chat += character;
        return chating;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button)
    {
        if (button == Input.Buttons.LEFT)
        {
            // Some stuff
            //System.out.println("Pew");
            ///player.Shoot();
            player.setShooting(true);
            return true;
        }
        if (button == Input.Buttons.RIGHT)
        {
            // Some stuff
            //System.out.println("Pew");
            //GameManager.getInstance().ClearProjectiles();

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (button == Input.Buttons.LEFT)
        {
            player.getGunEquipped().setHasShot(false);
            player.setShooting(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float deltaX = (int) player.getScreenPosition().x - screenX;
        float deltaY = (int) player.getScreenPosition().y - screenY;
        Vector2 delta = new Vector2(deltaX, deltaY);

        player.rotation = delta.angle();
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        float deltaX = (int) player.getScreenPosition().x - screenX;
        float deltaY = (int) player.getScreenPosition().y - screenY;
        Vector2 delta = new Vector2(deltaX, deltaY);

        player.rotation = delta.angle();
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
