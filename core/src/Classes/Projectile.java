package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

import java.rmi.RemoteException;

import static com.badlogic.gdx.utils.TimeUtils.millis;

/**
 * Created by Nick on 11-10-2016.
 */
public class Projectile extends GameObject
{
    private GameObject gameObject;
    private float bulletSpeed;
    private int damage;
    private Gun gun;
    private long lifeTime = 10000; // in millisec
    private long born; //in millisec

    public Projectile(Gun gun, Texture texture, Vector2 position, float rotation, Shape boundingShape) throws RemoteException
    {
        super (texture, position, rotation);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();
    }

    public Projectile(Gun gun, Vector2 position, float rotation, Shape boundingShape) throws RemoteException
    {
        super (position, rotation);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();
    }

    public Projectile(Gun gun, Vector2 position, float rotation) throws RemoteException
    {
        super (position, rotation);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();

        float width = 5;

        Vector2 v1 = new Vector2(width,width);
        Vector2 v2 = new Vector2(-width,width);
        Vector2 v3 = new Vector2(width,-width);
        Vector2 v4 = new Vector2(-width,-width);
        Vector2[] v5 = {v1,v2,v4,v3};

        setHitbox(v5);
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
        Vector2 rot = new Vector2(MathUtils.sin(rad),MathUtils.cos(rad));
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
            if (((Projectile)other).gun != null)
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
        else if (other instanceof Player )
        {
            System.out.println("Hit een speler");

            if (gun != null)
            {
                System.out.println("Gun niet null");

                if (gun.getOwner().getID() != other.getID())
                {
                    System.out.println("player is niet mijn owner");
                    System.out.println("Owner : " + gun.getOwner().getName());
                    System.out.println("Other : " + ((Player) other).getName());
                    System.out.println(new KillLog(this, (Player) other));
//                    try
//                    {
//                        GameManager.getInstance().ClearProjectile(this);
//                    }
//                    catch (RemoteException e)
//                    {
//                        e.printStackTrace();
//                    }
                    System.out.println("player gaat dood");
                    ((Player) other).Die();
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
