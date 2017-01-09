package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;

import static com.badlogic.gdx.utils.TimeUtils.millis;

/**
 * Created by Nick on 11-10-2016.
 */
public class Projectile extends GameObject
{
    private GameObject gameObject;
    private float bulletSpeed;
    private int damage = 1;
    private Gun gun;
    private long lifeTime = 10000; // in millisec
    private long born; //in millisec
    private float size = 5;

    public Projectile(Gun gun, Vector2 position, float rotation) throws RemoteException
    {
        super(position, rotation);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();

        setHitbox(DEFAULTHITBOX(size));
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

        if (millis() - born > lifeTime)
        {
            GameManager.getInstance().ClearProjectile(this);
        }

    }

    @Override
    public void onCollisionEnter(IGameObject other)
    {
        if (other instanceof Projectile)
        {
            if (((Projectile) other).gun != null)
            {
                if (((Projectile) other).gun.getOwner().getID() != gun.getOwner().getID())
                {
                    try
                    {
                        GameManager.getInstance().ClearProjectile(this);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
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
                e.printStackTrace();
            }

        }
        else if (other instanceof Player)
        {
            if (gun != null)
            {

                if (gun.getOwner().getID() != other.getID())
                {

                    System.out.println("Gun owner : " + gun.getOwner().getID());
                    System.out.println("Target : " + other.getID());
                    System.out.println("GameManager Player " + GameManager.getInstance().getPlayer().getID());

                    if (other.getID() == GameManager.getInstance().getPlayer().getID()) //geraakte speler moet weg gaan
                    {
                        System.out.println(new KillLog(this, (Player) other));
                        ((Player) other).Die();
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
                                    Thread.sleep(10);
                                    try
                                    {
                                        GameManager.getInstance().ClearProjectile(meProjectile);
                                    }
                                    catch (RemoteException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }.run();
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "Rekt";
    }
}
