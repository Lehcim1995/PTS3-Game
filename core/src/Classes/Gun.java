package Classes;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by myron on 11-10-16.
 */
public class Gun implements Serializable
{
    public static final Gun CZ75 = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BOLT_ACTION, true, 670, 10, null);
    public static final Gun SUPER = new Gun("SUPER", 0, 50, 0, 10, 500, gunType.AUTOMATIC, true, 1000, 1000, null);
    private String name;
    private float reloadTime;
    private float bulletsPerSecond;
    private float spread;
    private int currentBullets;
    private int maxBullets;
    private int totalBullets;
    private String shootType;
    private boolean hasInfinit;
    private Player owner;
    private float bulletSpeed;
    private int projectileDamage;
    private transient Thread reloadThread = null;
    private transient Thread shootThread = null;
    private transient long lastShot;
    private transient boolean reloading = false;
    private transient boolean hasShot = false;
    private gunType gunMode = gunType.AUTOMATIC;
    /**
     * Gun Constructor
     *
     * @param name - Gun name
     * @param reloadTime - Time it takes to reload the gun
     * @param bulletsPerSecond - Amount of bullets that can be shot in one sec
     * @param spread - Width range of a gun's bullets
     * @param currentBullets - Bullets a gun is having
     * @param maxBullets - Maximum amount of bullets the gun can have
     * @param gunMode - The mode of the gun
     * @param hasInfinit - If it has infinit ammo
     * @param bulletSpeed - Speed of the bullets
     * @param projectileDamage - Damage of one bullet
     * @param owner - Owner of the gun
     */
    public Gun(String name, float reloadTime, float bulletsPerSecond, float spread, int currentBullets, int maxBullets, gunType gunMode, boolean hasInfinit, float bulletSpeed, int projectileDamage, Player owner)
    {
        this.name = name;
        this.reloadTime = reloadTime;
        this.bulletsPerSecond = bulletsPerSecond;
        this.spread = spread;
        this.currentBullets = currentBullets;
        if (currentBullets > maxBullets)
        {
            this.currentBullets = maxBullets;
        }
        this.maxBullets = maxBullets;
        this.gunMode = gunMode;
        this.hasInfinit = hasInfinit;
        this.bulletSpeed = bulletSpeed;
        this.projectileDamage = projectileDamage;
        this.owner = owner;

        if (!hasInfinit)
        {
            totalBullets = maxBullets * 4;
        }
    }

    //<editor-fold desc="Getters & Setters">
    public String getName()
    {
        return name;
    }

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

    public boolean isHasShot()
    {
        return hasShot;
    }

    public void setHasShot(boolean hasShot)
    {
        this.hasShot = hasShot;
    }
    /**
     * Shooting the gun
     *
     */
    public void Shoot() throws RemoteException
    {
        if (millis() - lastShot > 1000 / bulletsPerSecond && (gunMode == gunType.AUTOMATIC || !hasShot) && currentBullets > 0 && !owner.reloadThread)
        {
            Random r = new Random();
            float rot = owner.getRotation();
            rot += (r.nextFloat() * spread) / 2;

            Projectile projectile = new Projectile(this, new Vector2(owner.getPosition().x, owner.getPosition().y), rot);

            GameManager.getInstance().addGameObject(projectile);
            currentBullets--;
            hasShot = true;
            lastShot = millis();
        }
    }
    /**
     * Reloading the gun
     *
     */
    public void Reload()
    {

        if (!owner.reloadThread && reloadThread == null || !reloadThread.isAlive())
        {
            owner.reloadThread = true;
            reloadThread = new Thread(() ->
            {
                if (currentBullets != maxBullets)
                {
                    if (currentBullets > 0)
                    {
                        totalBullets += currentBullets;
                    }
                    currentBullets = 0;
                    try
                    {
                        Thread.sleep((long) reloadTime);
                    }
                    catch (InterruptedException e)
                    {
                        LOGGER.log(Level.SEVERE,"Interruped ex: " + e);
                        Thread.currentThread().interrupt();
                    }

                    if (!hasInfinit)
                    {
                        if (totalBullets >= maxBullets)
                        {
                            currentBullets = maxBullets;
                            totalBullets -= maxBullets;
                        }
                        else
                        {
                            currentBullets = totalBullets;
                            totalBullets = 0;
                        }
                    }
                    else
                    {
                        currentBullets = maxBullets;
                    }
                }
                owner.reloadThread = false;
            });
            reloadThread.start();
        }
    }

    @Override
    public String toString()
    {
        return name + " " + " Ammo" + currentBullets + "/" + maxBullets;
    }
    /**
     * The type of a gun
     *
     */
    public enum gunType
    {
        AUTOMATIC,
        BOLT_ACTION
    }
}
