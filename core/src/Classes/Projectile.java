package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;
import java.util.logging.Level;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Nick on 11-10-2016.
 */
public class Projectile extends GameObject
{
    private float bulletSpeed;
    private int damage = 1;
    private Gun gun;
    private long lifeTime = 10000; // in millisec
    private long born; //in millisec
    private float size = 5;

    /**
     * Projectile Constructor
     *
     * @param gun      - The gun where the projectile belongs to.
     * @param position - Location Where the projectile begins.
     * @param rotation - The orientation the bullet is going to.
     */
    public Projectile(Gun gun, Vector2 position, float rotation) throws RemoteException
    {
        super(position, rotation);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();

        setHitbox(DefaultHitbox(size));
        born = millis();
    }

    public Gun getGun()
    {
        return gun;
    }

    @Override
    public void Draw(ShapeRenderer sr)
    {
        sr.setColor(Color.RED);
        sr.circle(position.x, position.y, 5);
    }

    @Override
    public void update() throws RemoteException
    {
        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2(MathUtils.sin(rad), MathUtils.cos(rad));
        position.add(rot.scl(bulletSpeed * Gdx.graphics.getDeltaTime()));

        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);

        if (position.x > Classes.Level.LevelSizeX || position.x < 0 || position.y > Classes.Level.LevelSizeY || position.y < 0)
        {
            GameManager.getInstance().ClearProjectile(this);
        }

        if (millis() - born > lifeTime)
        {
            GameManager.getInstance().ClearProjectile(this);
        }

    }

    @Override
    public void onCollisionEnter(IGameObject other) throws RemoteException
    {
        if (other instanceof Projectile)
        {
            if (((Projectile) other).gun != null && ((Projectile) other).gun.getOwner().getID() != gun.getOwner().getID())
            {
                try
                {
                    GameManager.getInstance().ClearProjectile(this);
                }
                catch (RemoteException e)
                {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        else if (other instanceof LevelBlock)
        {
            try
            {
                GameManager.getInstance().ClearProjectile(this);
            }
            catch (RemoteException e)
            {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }

        }
        else if (other instanceof Player &&
                gun != null && gun.getOwner().getID() != other.getID())
        {
            LOGGER.log(Level.INFO, "Gun owner : " + gun.getOwner().getID());
            LOGGER.log(Level.INFO, "Target : " + other.getID());
            LOGGER.log(Level.INFO, "GameManager Player " + GameManager.getInstance().getPlayer().getID());

            if (other.getID() == GameManager.getInstance().getPlayer().getID()) //geraakte speler moet weg gaan
            {
                LOGGER.log(Level.INFO, new KillLog(this, (Player) other).toString());
                ((Player) other).Die(this);
                gun.getOwner().Hit();
            }

            if (gun.getOwner().getID() == GameManager.getInstance().getPlayer().getID()) // Schieter moet zijn kogel weg halen
            {
                Projectile meProjectile = this;
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        try
                        {
                            Thread.sleep(20);
                            GameManager.getInstance().ClearProjectile(meProjectile);
                        }
                        catch (InterruptedException e)
                        {
                            LOGGER.log(Level.WARNING, e.getMessage(), e);
                            Thread.currentThread().interrupt();
                        }
                        catch (RemoteException e)
                        {
                            LOGGER.log(Level.WARNING, e.getMessage(), e);
                        }
                    }
                }.start();
            }
        }
    }

    @Override
    public String toString()
    {
        return "Rekt";
    }
}
