package com.asom.mygame;

import Classes.GameManager;
import Classes.Player;
import Classes.Projectile;
import Interfaces.IGameObject;
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

public class MainClass extends ApplicationAdapter
{
    SpriteBatch batch;
    Texture img;
    Vector2 position;
    Vector2 mouse;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Player player;

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
        player.Draw(shapeRenderer);
        for(IGameObject go :  GameManager.getInstance().GetProjectile())
        {
            ((Projectile)go).Draw(shapeRenderer);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.polygon(player.getHitbox().getTransformedVertices());
        for(IGameObject go :  GameManager.getInstance().GetProjectile())
        {
            shapeRenderer.polygon(((Projectile)go).getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();
    }

    public void update()
    {
        player.Update();
        //GameManager.getInstance().
        for(IGameObject go :  GameManager.getInstance().GetProjectile())
        {
            go.Update();
        }
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
