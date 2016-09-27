package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by michel on 27-9-2016.
 */
public class InputClass implements InputProcessor
{
    private IGameObject player;

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
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                //Right
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                //Up
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                //Down
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
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
