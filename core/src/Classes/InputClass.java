package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
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
            System.out.println("Pew");
            player.Shoot();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
