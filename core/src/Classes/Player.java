package Classes;

import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializableColor;
import LibGDXSerialzableClasses.SerializablePolygon;
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

import java.rmi.RemoteException;
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
    private transient Gun gunEquipped;
    private transient Random r = new Random();
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
    private float width = 34;
    private float halfWidth = width/2;
    private float quaterWidth = halfWidth / 2;
    private SerializableColor color = SerializableColor.DARK_GRAY;
    //
    private transient InputClass ic;
    private boolean shooting = false;

    public boolean reloadThread = false;

    private Vector2 lastpos;

    public Player(Vector2 position, float rotation) throws RemoteException
    {
        super(position, rotation);
        setHitbox(CIRCLEHITBOX(halfWidth));
    }

    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape, String name, Gun gunEquipped) throws RemoteException
    {
        super(texture, position, rotation);
        this.name = name;
        this.gunEquipped = gunEquipped;
    }

    public Player() throws RemoteException
    {
        super();
        //640,480
        position = new Vector2(r.nextInt(610) + 30, r.nextInt(450) + 30);
        lastpos = position;
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BoltAction, true, 670, 10, this);

        Gdx.input.setInputProcessor(ic);

        setHitbox(CIRCLEHITBOX(halfWidth));
    }

    public Player(Player p) throws RemoteException
    {
        super();
        //640,480
        position = p.position;
        lastpos = position;
        speed = 125.1248f;
        this.gunEquipped = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BoltAction, true, 670, 10, this);

        setHitbox(CIRCLEHITBOX(halfWidth));
    }

    public Player(Player p, boolean PlayerInput) throws RemoteException
    {
        super();
        //640,480
        position = p.position;
        lastpos = position;
        speed = 125.1248f;
        name = p.GetName();
        color = p.color;

        ic = new InputClass(this);
        this.gunEquipped = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BoltAction, true, 670, 10, this);
        if (PlayerInput)
        {
            Gdx.input.setInputProcessor(ic);
        }

        setHitbox(CIRCLEHITBOX(halfWidth));
    }

    public Player(boolean PlayerInput) throws RemoteException
    {
        super();
        //640,480
        position = new Vector2(r.nextInt(610) + 30, r.nextInt(450) + 30);
        lastpos = position;
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BoltAction, true, 670, 10, this);

        if (PlayerInput)
        {
            Gdx.input.setInputProcessor(ic);
        }

        setHitbox(CIRCLEHITBOX(halfWidth));
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

    public void Shoot() throws RemoteException
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
        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2((halfWidth + quaterWidth) * MathUtils.sin(rad), (halfWidth + quaterWidth) * MathUtils.cos(rad));
        //Vector2 rot2 = new Vector2((halfWidth - 1) * MathUtils.sin(rad), (halfWidth - 1) * MathUtils.cos(rad));
        sr.setColor(Color.RED);
        sr.rectLine(position.x, position.y, position.x + rot.x, position.y + rot.y, 8);

        sr.setColor(color.getLibGDXColor());
        sr.circle(position.x, position.y, halfWidth);

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
    public void Update() throws RemoteException
    {
        lastpos = position;
        Vector2 pos = new Vector2();
        //System.out.println("Update");

        if (ic != null)
        {
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
        }

        position.add(pos.scl(speed * Gdx.graphics.getDeltaTime()));
        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);

        if (position.x < 0)
        {
            position.x = 0;
        }

        if (position.y < 0)
        {
            position.y = 0;
        }

        if (position.x > Level.LevelSizeX)
        {
            position.x = Level.LevelSizeX;
        }

        if (position.y > Level.LevelSizeY)
        {
            position.y = Level.LevelSizeY;
        }

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

            Vector2 playerpos = new Vector2(position);
            Vector2 otherpos = ((Player)other).position;

            Vector2 diff = playerpos.sub(otherpos).setLength(200 * Gdx.graphics.getDeltaTime());
            position.add(diff);

            //System.out.println(((Player)other).position);
        }
        else if (other instanceof LevelBlock)
        {

            Vector2 playerpos = new Vector2(position);
            Vector2 otherpos = ((LevelBlock)other).position;
            LevelBlock lb = (LevelBlock)other;

            Vector2 diff = playerpos.sub(otherpos).setLength(200 * Gdx.graphics.getDeltaTime());
            position.add(diff);


            //System.out.println(((Player)other).position);
        }
    }

    public String GetName()
    {
        return this.name;
    }

    public void SetColor(SerializableColor color)
    {
        this.color = color;
    }

    public void SetName(String name)
    {
        this.name = name;
    }

    enum walkDir
    {
        Up,
        Down,
        Left,
        Right
    }
}
