package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.input.*;
import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by michel on 27-9-2016.
 */
public class Player extends GameObject
{
    //Normal vars
    private String name;
    private float health;
    private int maxHealth;
    private float speed;
    private float maxSpeed;
    private float acceleration;
    private float deAcceleration;
    private Gun gunEquipped;
    private Random r = new Random();
    //Game vars
    private int kills;
    private int deaths;
    private int shots;
    private int shotsHit;
    //implementation vars
    private boolean walkingUp;
    private boolean walkingDown;
    private boolean walkingLeft;
    private boolean walkingRight;
    //Appearance
    private float width = 17;
    private Color color = Color.BLACK;
    //
    private InputClass ic;
    private boolean shooting = false;

    public boolean reloadThread = false;


    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape)
    {
        super(texture, position, rotation, boundingShape);
    }

    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape, String name, Gun gunEquipped)
    {
        super(texture, position, rotation, boundingShape);
        this.name = name;
        this.gunEquipped = gunEquipped;
    }

    public Player()
    {
        super();
        //640,480
        position = new Vector2(r.nextInt(610) + 30, r.nextInt(450) + 30);
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = new Gun("kankergun", 2000, 1, 0, 10, 7, "kut", true, 670, 10, this);
        Gdx.input.setInputProcessor(ic);

        Vector2 v1 = new Vector2(width, width);
        Vector2 v2 = new Vector2(-width, width);
        Vector2 v3 = new Vector2(width, -width);
        Vector2 v4 = new Vector2(-width, -width);
        Vector2[] v5 = {v1, v2, v4, v3};

        setHitbox(v5);
        //setOrigin(new Vector2(position.x - width/2,position.y + width/2 ));
    }

    public void Walk(walkDir dir, boolean setWalking)
    {
        switch (dir)
        {
            case Up:
                walkingUp = setWalking;
                break;
            case Down:
                walkingDown = setWalking;
                break;
            case Left:
                walkingLeft = setWalking;
                break;
            case Right:
                walkingRight = setWalking;
                break;
            default:
                break;
        }
    }

    public void Shoot()
    {
        this.gunEquipped.Shoot();
    }

    public void Reload()
    {
        gunEquipped.Reload();

    }

    public void Spawn()
    {
        position = new Vector2(r.nextInt(610) + 17, r.nextInt(450) + 17);
    }

    public void Die()
    {
        health = 0;
        deaths ++;
        GameManager.getInstance().SpawnPlayer(this);
    }

    public void Hit()
    {

    }

    @Override
    public void Draw(ShapeRenderer sr)
    {

        sr.setColor(color);
        //shapeRenderer.line(position.x, position.y, mous
        // e.x, mouse.y);
        sr.circle(position.x, position.y, width);

        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2(width * MathUtils.sin(rad), width * MathUtils.cos(rad));
        sr.setColor(Color.RED);
        sr.line(position.x, position.y, position.x + rot.x, position.y + rot.y);


        //sr.dispose();
    }

    public Gun getGunEquipped()
    {
        return gunEquipped;
    }

    public boolean isShooting()
    {
        return shooting;
    }

    public void setShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    @Override
    public void Update()
    {
        Vector2 pos = new Vector2();
        //System.out.println("Update");
        if (ic.GetKey(Input.Keys.W))
        {
            pos.y += 1;
        }
        if (ic.GetKey(Input.Keys.S))
        {
            pos.y -= 1;
        }
        if (ic.GetKey(Input.Keys.A))
        {
            pos.x -= 1;
        }
        if (ic.GetKey(Input.Keys.D))
        {
            pos.x += 1;
        }
        if (ic.GetKey(Input.Keys.R))
        {
            Reload();
        }
        if (ic.GetKey(Input.Keys.P))
        {
            Spawn();
        }



        position.add(pos.scl(speed * Gdx.graphics.getDeltaTime()));
        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);

        if (shooting)
        {
            Shoot();
        }

    }

    @Override
    public void OnCollisionEnter(IGameObject other)
    {
        if (other instanceof Player)
        {
            System.out.println("Collision");
        }
    }

    public String GetName()
    {
        return this.name;
    }

    enum walkDir
    {
        Up,
        Down,
        Left,
        Right
    }
}
