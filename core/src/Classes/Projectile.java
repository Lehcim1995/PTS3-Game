package Classes;

import com.badlogic.gdx.graphics.Texture;
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
}
