package Classes;

/**
 * Created by Nick on 11-10-2016.
 */
public class Projectile
{
    private GameObject gameObject;
    private float bulletSpeed;
    private int damage;
    private Gun gun;

    public Projectile(Gun gun)
    {
        this.gun = gun;
    }
}
