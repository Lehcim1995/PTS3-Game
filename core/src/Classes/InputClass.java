package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by michel on 27-9-2016.
 */
public class InputClass implements InputProcessor
{
    private Player player;
    private HashMap<Integer,Boolean> KeymapHold;
    private HashMap<Integer,Date> KeymapUp;
    private HashMap<Integer,Date> KeymapDown;
    private Timer timer;

    private final float holdDownTimer = 15;

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
        //System.out.println("Keydown");
        KeymapHold.put(keycode, true);
        //KeymapDown.put(keycode, Calendar.getInstance().getTime());
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        KeymapHold.put(keycode, false);
        //KeymapUp.put(keycode, Calendar.getInstance().getTime());

        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // Some stuff
            //System.out.println("Pew");
            ///player.Shoot();
            player.setShooting(true);
            return true;
        }
        if (button == Input.Buttons.RIGHT) {
            // Some stuff
            //System.out.println("Pew");
            GameManager.getInstance().ClearProjectiles();

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (button == Input.Buttons.LEFT) {
            player.getGunEquipped().setHasShot(false);
            player.setShooting(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float deltaX = (int)player.GetScreenPosition().x - screenX;
        float deltaY = (int)player.GetScreenPosition().y - screenY;
        Vector2 delta = new Vector2(deltaX, deltaY);

        player.rotation = delta.angle();
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        float deltaX = (int)player.GetScreenPosition().x - screenX;
        float deltaY = (int)player.GetScreenPosition().y - screenY;
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
