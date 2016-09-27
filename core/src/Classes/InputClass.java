package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by michel on 27-9-2016.
 */
public class InputClass implements InputProcessor
{
    private Player player;

    public InputClass()
    {
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                //Left
                player.Walk(Player.walkDir.Left, true);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                //Right
                player.Walk(Player.walkDir.Right, true);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                //Up
                player.Walk(Player.walkDir.Up, true);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                //Down
                player.Walk(Player.walkDir.Down, true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                //Left
                player.Walk(Player.walkDir.Left, false);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                //Right
                player.Walk(Player.walkDir.Right, false);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                //Up
                player.Walk(Player.walkDir.Up, false);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                //Down
                player.Walk(Player.walkDir.Down, false);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
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
