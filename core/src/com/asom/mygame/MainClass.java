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
    Texture img;
    Vector2 position;
    Vector2 mouse;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private Player enemy;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        img = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));
        position = new Vector2(0, 0);
        mouse = new Vector2(0, 0);
        camera = new OrthographicCamera();
        shapeRenderer = new ShapeRenderer();
        //Shape s = new CircleShape();
        //player = new Player(img, position, 0, s);
        player = new Player();
        enemy = new Player();
        GameManager.getInstance().addGameObject(player);
        GameManager.getInstance().addGameObject(enemy);
    }

    @Override
    public void render()
    {
        update();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.draw(img, position.x, position.y);s
        batch.end();

        //camera.update();
        //shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Iterator<GameObject> iterator = GameManager.getInstance().getObjects().iterator(); iterator.hasNext(); )
        {
            GameObject go = iterator.next();
            go.Draw(shapeRenderer);
        }

        shapeRenderer.end();
/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Classes.GameObject go :  GameManager.getInstance().getObjects())
        {
            //shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();*/
    }

    public void update()
    {
        GameManager.getInstance().Update();
    }

    private void inputhandler()
    {
        mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            position.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            position.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            position.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            position.y -= 1;
        }

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
        shapeRenderer.dispose();

    }

}
