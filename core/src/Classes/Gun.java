package Classes;

/**
 * Created by myron on 11-10-16.
 */
public class Gun
{
    private float reloadTime;
    private float bulletsPerSecond;
    private float spread;
    private int currentBullets;
    private int maxBullets;
    private String shootType;
    private boolean hasInfinit;
    private Player owner;
    private float bulletSpeed;
    private int projectileDamage;
    //<editor-fold desc="Getters & Setters">
    public float getReloadTime()
    {
        return reloadTime;
    }

    public float getBulletsPerSecond()
    {
        return bulletsPerSecond;
    }

    public float getSpread()
    {
        return spread;
    }

    public int getCurrentBullets()
    {
        return currentBullets;
    }

    public int getMaxBullets()
    {
        return maxBullets;
    }

    public String getShootType()
    {
        return shootType;
    }

    public boolean isHasInfinit()
    {
        return hasInfinit;
    }

    public Player getOwner()
    {
        return owner;
    }

    public void setOwner(Player owner)
    {
        this.owner = owner;
    }

    public float getBulletSpeed()
    {
        return bulletSpeed;
    }

    public int getProjectileDamage()
    {
        return projectileDamage;
    }

    //</editor-fold>
    public Gun(float reloadTime, float bulletsPerSecond, float spread, int currentBullets, int maxBullets, String shootType, boolean hasInfinit, float bulletSpeed, int projectileDamage)
    {
        this.reloadTime = reloadTime;
        this.bulletsPerSecond = bulletsPerSecond;
        this.spread = spread;
        this.currentBullets = currentBullets;
        this.maxBullets = maxBullets;
        this.shootType = shootType;
        this.hasInfinit = hasInfinit;
        this.bulletSpeed = bulletSpeed;
        this.projectileDamage = projectileDamage;
    }
    public void Shoot()
    {

    };
    public void Reload()
    {

    };
}
