package Classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.Timer;

import static com.badlogic.gdx.utils.TimeUtils.millis;

/**
 * Created by myron on 11-10-16.
 */
public class Gun
{
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

    private Thread reloadThread = null;
    private Thread shootThread = null;
    private long lastShot;
    private boolean reloading = false;
    private boolean hasShot = false;

    public enum gunType
    {
        Automatic,
        BoltAction
    }

    private gunType GunMode = gunType.Automatic;

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
    public Gun(String name, float reloadTime, float bulletsPerSecond, float spread, int currentBullets, int maxBullets, String shootType, boolean hasInfinit, float bulletSpeed, int projectileDamage, Player owner)
    {
        this.name = name;
        this.reloadTime = reloadTime;
        this.bulletsPerSecond = bulletsPerSecond;
        this.spread = spread;
        this.currentBullets = currentBullets;
        if (currentBullets > maxBullets) {
            this.currentBullets = maxBullets;
        }
        this.maxBullets = maxBullets;
        this.shootType = shootType;
        this.hasInfinit = hasInfinit;
        this.bulletSpeed = bulletSpeed;
        this.projectileDamage = projectileDamage;
        this.owner = owner;

        if (!hasInfinit) {
            totalBullets = maxBullets * 4;
        }
    }

    public void setHasShot(boolean hasShot)
    {
        this.hasShot = hasShot;
    }

    public boolean isHasShot()
    {
        return hasShot;
    }

    public void Shoot()
    {

        System.out.println("pew");

        if (millis() - lastShot > 1000 / bulletsPerSecond && (GunMode == gunType.Automatic || !hasShot) && currentBullets > 0 && !owner.reloadThread)
        {
            System.out.println("    pew");
            Projectile projectile = new Projectile(this, new Vector2(owner.GetPosition().x, owner.GetPosition().y), owner.GetRotation());

            GameManager.getInstance().AddProjectile(projectile);
            currentBullets--;
            hasShot = true;
            lastShot = millis();
        }
    }

    public void Reload()
    {

        if(!owner.reloadThread)
        {
            if (reloadThread == null || !reloadThread.isAlive())
            {
                owner.reloadThread = true;
                reloadThread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        if (currentBullets != maxBullets) {
                            System.out.println("Reloading");
                            if (currentBullets > 0) {
                                totalBullets += currentBullets;
                            }
                            currentBullets = 0;
                            try
                            {
                                Thread.sleep((long) reloadTime);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                            if (!hasInfinit) {
                                if (totalBullets >= maxBullets) {
                                    currentBullets = maxBullets;
                                    totalBullets -= maxBullets;
                                }
                                else
                                {
                                    currentBullets = totalBullets;
                                    totalBullets = 0;
                                }
                            }
                            else {
                                currentBullets = maxBullets;
                            }
                        }
                        owner.reloadThread = false;
                    }
                });
                reloadThread.start();
            }
        }
    }

    public String ToString()
    {
        return name + " " + " Ammo" + currentBullets + "/" + maxBullets;
    }
}
