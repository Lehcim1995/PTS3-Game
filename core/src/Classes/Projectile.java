package Classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
    }

    public Gun GetGun()
    {
        return gun;
    }

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
    }
}
