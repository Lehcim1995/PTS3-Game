package Scenes;

import Interfaces.IConnection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.rmi.registry.Registry;

/**
 * Created by Nick on 22-11-2016.
 */
public abstract class AbstractScreen extends Stage implements Screen
{

    Registry registry;
    IConnection conn;

    protected AbstractScreen()
    {
        super(new StretchViewport(500.0f, 500.0f, new OrthographicCamera()));
        registry = ScreenManager.getInstance().GetRegistry();
        conn = ScreenManager.getInstance().GetConnection();
    }

    public abstract void buildStage();

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        getViewport().update(width, height, true);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }
}
