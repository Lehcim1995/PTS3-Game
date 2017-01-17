package Classes;

import Interfaces.IGameObject;
import Interfaces.IServer;
import LibGDXSerialzableClasses.SerializableColor;
import Scenes.GameSceneScreen;
import Scenes.ScreenEnum;
import Scenes.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

/**
 * Created by michel on 27-9-2016.
 */
public class Player extends GameObject
{
    public boolean reloadThread = false;
    //Normal vars
    private String name;
    private float health;
    private int maxHealth;
    private float speed;
    private float maxSpeed;
    private float acceleration;
    private float deAcceleration;
    private Gun gunEquipped;
    private transient Random r = new Random();
    //Game vars
    private int kills = 0;
    private int deaths = 0;
    private int shots = 0;
    private int shotsHit = 0;
    //Appearance
    private float width = 34;
    private float halfWidth = width / 2;
    private float quaterWidth = halfWidth / 2;
    private float barrelLenght = 8;
    private SerializableColor color = SerializableColor.DARK_GRAY;
    //
    private transient InputClass ic;
    private boolean shooting = false;
    private transient BitmapFont font = new BitmapFont();
    private transient GlyphLayout layout = new GlyphLayout();

    public Player(Vector2 position, float rotation) throws RemoteException
    {
        super(position, rotation);
        setHitbox(CircleHitbox(halfWidth));
    }

    public Player() throws RemoteException
    {
        super();
        //640,480
        position = new Vector2(r.nextInt(610) + 30, r.nextInt(450) + 30);
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = Gun.CZ75;
        this.getGunEquipped().setOwner(this);

        Gdx.input.setInputProcessor(ic);
        setHitbox(CircleHitbox(halfWidth));

    }

    public Player(Player p) throws RemoteException
    {
        super();
        //640,480
        position = p.position;
        speed = 125.1248f;
        this.gunEquipped = Gun.CZ75;
        this.getGunEquipped().setOwner(this);
        setHitbox(CircleHitbox(halfWidth));
    }

    public Player(Player p, boolean PlayerInput) throws RemoteException
    {
        super();
        //640,480
        position = p.position;
        speed = 125.1248f;
        name = p.getName();
        color = p.color;

        ic = new InputClass(this);
        this.gunEquipped = Gun.CZ75;
        this.getGunEquipped().setOwner(this);

        if (PlayerInput)
        {
            Gdx.input.setInputProcessor(ic);
        }

        setHitbox(CircleHitbox(halfWidth));
    }

    public Player(boolean PlayerInput) throws RemoteException
    {
        super();
        //640,480
        position = new Vector2(r.nextInt(610) + 30, r.nextInt(450) + 30);
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = Gun.CZ75;
        this.getGunEquipped().setOwner(this);

        if (PlayerInput)
        {
            Gdx.input.setInputProcessor(ic);
        }
        setHitbox(CircleHitbox(halfWidth));
    }

    public void Shoot() throws RemoteException
    {
        this.gunEquipped.Shoot();
        shots++;
    }

    public void Reload()
    {
        gunEquipped.Reload();
    }

    public void Spawn()
    {

        if (r == null)
        {
            r = new Random();
        }
        position = new Vector2(r.nextInt(610) + 17, r.nextInt(450) + 17);
    }

    public void Die(Projectile projectile) throws RemoteException
    {
        health = 0;
        deaths++;
        KillLog kl = new KillLog(projectile, this);
        GameManager.getInstance().addGameObject(kl);
//        GameManager.getInstance().AddKillLog(kl);
        GameManager.getInstance().SpawnPlayer(this);
    }

    public void Hit()
    {
        kills++;
        shotsHit++;
        System.out.println("Hit player killed by: " + name + " Kills: " + kills + " shotsHit: " + shotsHit);
    }

    @Override
    public void Draw(ShapeRenderer sr)
    {
        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2((halfWidth + quaterWidth) * MathUtils.sin(rad), (halfWidth + quaterWidth) * MathUtils.cos(rad));
        //Vector2 rot2 = new Vector2((halfWidth - 1) * MathUtils.sin(rad), (halfWidth - 1) * MathUtils.cos(rad));
        sr.setColor(Color.RED);
        sr.rectLine(position.x, position.y, position.x + rot.x, position.y + rot.y, barrelLenght);

        sr.setColor(color.getLibGDXColor());
        sr.circle(position.x, position.y, halfWidth);
    }

    public void Draw(ShapeRenderer sr, Batch bc)
    {
        Draw(sr);

        if (font != null && layout != null)
        {
            bc.begin();
            font.setColor(Color.BLACK);
            String text = GameManager.getInstance().getPlayer().getGunEquipped().toString();
            layout.setText(font, text);
            float width = layout.width;// contains the width of the current set text
            float height = layout.height; // contains the height of the current set text
            font.draw(bc, layout, GameManager.getInstance().getCamera().viewportWidth - width, height);
            bc.end();
        }

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
    public void update() throws RemoteException
    {
        Vector2 pos = new Vector2();

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
            if (ic.GetKey(Input.Keys.ESCAPE))
            {
                //leave game
                GameManager.getInstance().stop();
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
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
    public void onCollisionEnter(IGameObject other)
    {
        if (other instanceof Player)
        {

            Vector2 playerpos = new Vector2(position);
            Vector2 otherpos = ((Player) other).position;

            Vector2 diff = playerpos.sub(otherpos).setLength(200 * Gdx.graphics.getDeltaTime());
            position.add(diff);
        }
        else if (other instanceof LevelBlock)
        {
            Vector2 playerpos = new Vector2(position);
            Vector2 otherpos = ((LevelBlock) other).position;
            LevelBlock lb = (LevelBlock) other;

            Vector2 diff = playerpos.sub(otherpos).setLength(200 * Gdx.graphics.getDeltaTime());
            position.add(diff);
        }
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setColor(SerializableColor color)
    {
        this.color = color;
    }
}
