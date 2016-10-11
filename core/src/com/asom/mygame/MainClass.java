package com.asom.mygame;

import Classes.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;

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
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(position.x, position.y, mouse.x, mouse.y);
        shapeRenderer.circle(position.x, position.y, 15);
        shapeRenderer.end();
    }

    public void update()
    {
        inputhandler();
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
