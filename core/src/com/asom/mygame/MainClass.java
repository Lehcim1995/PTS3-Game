package com.asom.mygame;

import Classes.*;
import Interfaces.IGameObject;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.rmi.RemoteException;
import java.util.Iterator;

public class MainClass extends Game implements ApplicationListener{
    SpriteBatch batch;
    //Texture img;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private float zoom = 1;


    @SuppressWarnings("Duplicates")
    @Override
    public void create() {
        batch = new SpriteBatch();
        //img = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w * zoom, h * zoom);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, zoom);
        camera.update();

        shapeRenderer = new ShapeRenderer();
    }



    @Override
    public void render() {
        try {
            synchronized (this)
            {
                update();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (GameManager.getInstance().getPlayer() != null) {
            camera.position.set(GameManager.getInstance().getPlayer().getPosition().x, GameManager.getInstance().getPlayer().getPosition().y, 1);
        }
        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Iterator<IGameObject> iterator = GameManager.getInstance().getAllObjects().iterator(); iterator.hasNext(); ) {
            IGameObject go = iterator.next();
            try
            {
                go.Draw(shapeRenderer);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        shapeRenderer.end();



        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for(IGameObject go :  GameManager.getInstance().getAllObjects())
        {
            shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void update() throws RemoteException {
        GameManager.getInstance().Update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //img.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height * zoom;
        camera.viewportWidth = width * zoom;
    }
}
