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

/**
 * Created by Nick on 11-10-2016.
 */
public class Projectile extends GameObject
{
    private GameObject gameObject;
    private float bulletSpeed;
    private int damage;
    private Gun gun;

    public Projectile(Gun gun, Texture texture, Vector2 position, float rotation, Shape boundingShape)
    {
        super (texture, position, rotation, boundingShape);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();
    }

    public Projectile(Gun gun, Vector2 position, float rotation, Shape boundingShape)
    {
        super (position, rotation, boundingShape);
        this.gun = gun;
        bulletSpeed = gun.getBulletSpeed();
        damage = gun.getProjectileDamage();
    }

    public Projectile(Gun gun, Vector2 position, float rotation)
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

    }

    public Gun GetGun()
    {
        return gun;
    }

    @Override
    public void Draw(ShapeRenderer sr)
    {
        sr.setColor(Color.GREEN);
        sr.circle(position.x, position.y, 5);
    }

    @Override
    public void Update()
    {
        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2(MathUtils.sin(rad),MathUtils.cos(rad));
        position.add(rot.scl(bulletSpeed * Gdx.graphics.getDeltaTime()));

        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);

    }

    @Override
    public void OnCollisionEnter(IGameObject other)
    {
        if (other instanceof Projectile)
        {
            if (((Projectile)other).gun.getOwner() != gun.getOwner() )
            {
                GameManager.getInstance().ClearProjectile(this);
            }
        }
        else if (other instanceof LevelBlock)
        {
            GameManager.getInstance().ClearProjectile(this);

        }
        else if (other instanceof Player && gun.getOwner() != other)
        {
            System.out.println(new KillLog(this, (Player) other));
            GameManager.getInstance().ClearProjectile(this);
            ((Player) other).Die();
        }
    }
}
