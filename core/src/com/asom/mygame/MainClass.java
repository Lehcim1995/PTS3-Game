package com.asom.mygame;

import Classes.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.Iterator;

public class MainClass extends ApplicationAdapter
{
    SpriteBatch batch;
    //Texture img;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private Player enemy;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        //img = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w,h);
        //camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 1);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        enemy = new Player();
        player = new Player();
        GameManager.getInstance().addGameObject(player);
        GameManager.getInstance().addGameObject(enemy);
    }

    @Override
    public void render()
    {
        update();
        camera.position.set(player.GetPosition().x, player.GetPosition().y, 1);
        camera.update();


        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.setProjectionMatrix(camera.combined);
        //batch.begin();
        //batch.draw(img, position.x, position.y);s
        //batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Iterator<GameObject> iterator = GameManager.getInstance().getObjects().iterator(); iterator.hasNext(); )
        {
            GameObject go = iterator.next();
            go.Draw(shapeRenderer);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Classes.GameObject go :  GameManager.getInstance().getObjects())
        {
            shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();
    }

    public void update()
    {
        GameManager.getInstance().Update();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        //img.dispose();
        shapeRenderer.dispose();
    }

}
